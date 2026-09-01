<template>
  <Dialog title="出库单打印" v-model="dialogVisible" width="860px" top="5vh">
    <div v-loading="formLoading" class="printer-wrap">
      <div class="printer-toolbar no-print">
        <el-button type="primary" v-print="printObj">
          <Icon icon="ep:printer" class="mr-5px" /> 打印
        </el-button>
      </div>

      <div id="stockout-print-area" class="print-sheet">
        <h1 class="print-sheet__title">出库单</h1>

        <div class="print-sheet__meta">
          <div class="print-sheet__meta-left">
            <div>客户：{{ customerName }}</div>
          </div>
          <div class="print-sheet__meta-center">
            <div>日期：{{ outboundDateText }}</div>
          </div>
          <div class="print-sheet__meta-right">
            <div class="flex flex-col items-center">
              <div>
                <Barcode
                  :content="formData.docnum"
                  :display-value="false"
                  :height="100"
                  :width="180"
                />
              </div>
              <div class="text-xs -mt-2">条形码：{{ formData.docnum || '-' }}</div>
            </div>
          </div>
        </div>

        <table class="print-table">
          <thead>
            <tr>
              <th style="width: 6%">序号</th>
              <th style="width: 10%">商品代码</th>
              <th style="width: 14%">商品名称</th>
              <th style="width: 12%">规格型号</th>
              <th style="width: 6%">单位</th>
              <th style="width: 8%">数量</th>
              <th style="width: 10%">单价</th>
              <th style="width: 10%">金额</th>
              <th style="width: 14%">备注</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, index) in detailList" :key="index">
              <td>{{ index + 1 }}</td>
              <td>{{ item.goodsCode || '' }}</td>
              <td>{{ item.goodsName || '' }}</td>
              <td>{{ item.spec || '' }}</td>
              <td>{{ item.unitName || '' }}</td>
              <td>{{ formatQty(item.stockOutNums) }}</td>
              <td>{{ formatMoney(item.outboundPrice) }}</td>
              <td>{{ formatMoney(item.amount) }}</td>
              <td>{{ item.stockOutGoodsRemark || '' }}</td>
            </tr>
            <tr v-if="!detailList.length">
              <td colspan="9" class="empty-cell">暂无明细</td>
            </tr>
            <tr class="sum-row">
              <td colspan="5" class="sum-label">本页小计：</td>
              <td>{{ formatQty(pageQty) }}</td>
              <td></td>
              <td>{{ formatMoney(pageAmount) }}</td>
              <td></td>
            </tr>
            <tr class="sum-row">
              <td colspan="5" class="sum-label">合&nbsp;&nbsp;计：</td>
              <td>{{ formatQty(totalQty) }}</td>
              <td></td>
              <td>{{ formatMoney(totalAmount) }}</td>
              <td></td>
            </tr>
          </tbody>
        </table>

        <div class="print-sheet__footer">
          <span>负责人：</span>
          <span>经办人：</span>
          <span>库管员：</span>
        </div>
      </div>
    </div>
  </Dialog>
</template>

<script setup lang="ts">
import { formatDate } from '@/utils/formatTime'
import { StockoutApi } from '@/api/stock/stockstockout'

/** 商品出库 打印预览 */
defineOptions({ name: 'StockoutPrinter' })

const dialogVisible = ref(false)
const formLoading = ref(false)

const formData = ref({
  id: undefined as number | undefined,
  docnum: undefined as string | undefined,
  stockCustomerName: undefined as string | undefined,
  outboundTime: undefined as number | string | Date | undefined,
  totalOutAmount: undefined as number | undefined
})

interface DetailItem {
  goodsCode: string
  goodsName: string
  spec: string
  stockOutNums?: number
  unitName: string
  outboundPrice?: number
  amount?: number
  stockOutGoodsRemark?: string
}

const detailList = ref<DetailItem[]>([])

const printObj = {
  id: 'stockout-print-area',
  popTitle: '出库单',
  extraCss: '',
  extraHead: '<meta charset="utf-8" />',
  preview: false,
  zIndex: 20002
}

const customerName = computed(() => formData.value.stockCustomerName || '-')

const outboundDateText = computed(() => {
  if (!formData.value.outboundTime) return '-'
  return formatDate(formData.value.outboundTime as any, 'YYYY-MM-DD')
})

const currentPage = 1
const totalPages = 1

const pageQty = computed(() =>
  detailList.value.reduce((sum, item) => sum + Number(item.stockOutNums || 0), 0)
)
const pageAmount = computed(() =>
  detailList.value.reduce((sum, item) => sum + Number(item.amount || 0), 0)
)
const totalQty = computed(() => pageQty.value)
const totalAmount = computed(() => {
  if (formData.value.totalOutAmount !== undefined && formData.value.totalOutAmount !== null) {
    return Number(formData.value.totalOutAmount)
  }
  return pageAmount.value
})

const formatMoney = (val?: number | string) => {
  if (val === undefined || val === null || val === '') return ''
  return Number(val).toFixed(2)
}

const formatQty = (val?: number | string) => {
  if (val === undefined || val === null || val === '') return ''
  const num = Number(val)
  return Number.isInteger(num) ? String(num) : num.toFixed(2)
}

const resetForm = () => {
  formData.value = {
    id: undefined,
    docnum: undefined,
    stockCustomerName: undefined,
    outboundTime: undefined,
    totalOutAmount: undefined
  }
  detailList.value = []
}

/** 打开打印弹窗 */
const open = async (id: number) => {
  dialogVisible.value = true
  resetForm()
  formLoading.value = true
  try {
    const data = await StockoutApi.getStockout(id)
    formData.value = {
      id: data.id,
      docnum: data.docnum,
      stockCustomerName: data.stockCustomerName,
      outboundTime: data.outboundTime,
      totalOutAmount: data.totalOutAmount
    }
    const lists = (data as any).stockStockoutListList || (data as any).items || []
    detailList.value = (lists || []).map((item: any) => ({
      goodsCode: item.goodsCode || String(item.stockGoodsId || ''),
      goodsName: item.stockGoodsName || item.goodsName || '',
      spec: item.stockGoodsValueName || item.spec || '',
      stockOutNums: item.stockOutNums,
      unitName: item.unitName || '件',
      outboundPrice: item.outboundPrice,
      amount: item.amount,
      stockOutGoodsRemark: item.stockOutGoodsRemark
    }))
  } finally {
    formLoading.value = false
  }
}
defineExpose({ open })
</script>

<style scoped lang="scss">
.printer-wrap {
  min-height: 200px;
}

.printer-toolbar {
  margin-bottom: 12px;
}

.print-sheet {
  color: #000;
  background: #fff;
  padding: 8px 12px 16px;
  font-size: 13px;
  line-height: 1.4;
  font-family: 'SimSun', 'Songti SC', 'Microsoft YaHei', sans-serif;
}

.print-sheet__title {
  margin: 0 0 16px;
  text-align: center;
  font-size: 28px;
  font-weight: 700;
  letter-spacing: 8px;
}

.print-sheet__meta {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 10px;
  font-size: 14px;
}

.print-sheet__meta-left,
.print-sheet__meta-center {
  flex: 1;
}

.print-sheet__meta-right {
  flex: 1;
  text-align: right;
  line-height: 1.6;
}

.print-table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
  font-size: 13px;

  th,
  td {
    border: 1px solid #000;
    padding: 6px 4px;
    text-align: center;
    word-break: break-all;
    vertical-align: middle;
  }

  th {
    font-weight: 600;
    background: #fff;
  }

  .empty-cell {
    padding: 16px 0;
    color: #999;
  }

  .sum-label {
    text-align: left;
    padding-left: 12px;
  }
}

.print-sheet__footer {
  display: flex;
  justify-content: space-between;
  margin-top: 28px;
  padding: 0 8px;
  font-size: 14px;
}
</style>

<!-- 打印样式：供 vue3-print-nb iframe 拷贝，A5 横向 -->
<style lang="scss">
@media print {
  @page {
    size: A5 landscape;
    margin: 8mm;
  }

  html,
  body {
    margin: 0 !important;
    padding: 0 !important;
    background: #fff !important;
  }

  .no-print {
    display: none !important;
  }
}

#stockout-print-area {
  color: #000;
  background: #fff;
  font-size: 13px;
  line-height: 1.4;
  font-family: 'SimSun', 'Songti SC', 'Microsoft YaHei', sans-serif;
}

#stockout-print-area .print-sheet__title {
  margin: 0 0 16px;
  text-align: center;
  font-size: 28px;
  font-weight: 700;
  letter-spacing: 8px;
}

#stockout-print-area .print-sheet__meta {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 10px;
  font-size: 14px;
}

#stockout-print-area .print-sheet__meta-left,
#stockout-print-area .print-sheet__meta-center {
  flex: 1;
}

#stockout-print-area .print-sheet__meta-right {
  flex: 1;
  text-align: right;
  line-height: 1.6;
}

#stockout-print-area .print-table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
  font-size: 13px;
}

#stockout-print-area .print-table th,
#stockout-print-area .print-table td {
  border: 1px solid #000;
  padding: 6px 4px;
  text-align: center;
  word-break: break-all;
  vertical-align: middle;
}

#stockout-print-area .print-table th {
  font-weight: 600;
}

#stockout-print-area .print-table .sum-label {
  text-align: left;
  padding-left: 12px;
}

#stockout-print-area .print-sheet__footer {
  display: flex;
  justify-content: space-between;
  margin-top: 28px;
  padding: 0 8px;
  font-size: 14px;
}
</style>
