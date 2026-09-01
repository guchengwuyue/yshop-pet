<template>
  <Dialog title="盘库单打印" v-model="dialogVisible" width="860px" top="5vh">
    <div v-loading="formLoading" class="printer-wrap">
      <div class="printer-toolbar no-print">
        <el-button type="primary" v-print="printObj">
          <Icon icon="ep:printer" class="mr-5px" /> 打印
        </el-button>
      </div>

      <div id="stockcheck-print-area" class="print-sheet">
        <h1 class="print-sheet__title">盘库单</h1>

        <div class="print-sheet__meta">
          <div class="print-sheet__meta-left">
            <div>盘盈：{{ formData.profitNums ?? 0 }}</div>
            <div>盘亏：{{ formData.lossNums ?? 0 }}</div>
          </div>
          <div class="print-sheet__meta-center">
            <div>日期：{{ checkDateText }}</div>
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
              <th style="width: 12%">商品代码</th>
              <th style="width: 16%">商品名称</th>
              <th style="width: 12%">规格型号</th>
              <th style="width: 6%">单位</th>
              <th style="width: 10%">账面数量</th>
              <th style="width: 10%">实盘数量</th>
              <th style="width: 8%">盈亏</th>
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
              <td>{{ formatQty(item.bookNums) }}</td>
              <td>{{ formatQty(item.actualNums) }}</td>
              <td>{{ formatQty(item.diffNums) }}</td>
              <td>{{ item.checkGoodsRemark || '' }}</td>
            </tr>
            <tr v-if="!detailList.length">
              <td colspan="9" class="empty-cell">暂无明细</td>
            </tr>
            <tr class="sum-row">
              <td colspan="5" class="sum-label">合&nbsp;&nbsp;计：</td>
              <td>{{ formatQty(totalBook) }}</td>
              <td>{{ formatQty(totalActual) }}</td>
              <td>{{ formatQty(totalDiff) }}</td>
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
import { StockCheckApi } from '@/api/stock/stockcheck'
import Barcode from '@/views/components/Barcode.vue'

/** 商品盘库 打印预览 */
defineOptions({ name: 'StockCheckPrinter' })

const dialogVisible = ref(false)
const formLoading = ref(false)

const formData = ref({
  id: undefined as number | undefined,
  docnum: undefined as string | undefined,
  checkTime: undefined as number | string | Date | undefined,
  profitNums: undefined as number | undefined,
  lossNums: undefined as number | undefined
})

interface DetailItem {
  goodsCode: string
  goodsName: string
  spec: string
  unitName: string
  bookNums?: number
  actualNums?: number
  diffNums?: number
  checkGoodsRemark?: string
}

const detailList = ref<DetailItem[]>([])

const printObj = {
  id: 'stockcheck-print-area',
  popTitle: '盘库单',
  extraCss: '',
  extraHead: '<meta charset="utf-8" />',
  preview: false,
  zIndex: 20002
}

const checkDateText = computed(() => {
  if (!formData.value.checkTime) return '-'
  return formatDate(formData.value.checkTime as any, 'YYYY-MM-DD')
})

const totalBook = computed(() =>
  detailList.value.reduce((sum, item) => sum + Number(item.bookNums || 0), 0)
)
const totalActual = computed(() =>
  detailList.value.reduce((sum, item) => sum + Number(item.actualNums || 0), 0)
)
const totalDiff = computed(() =>
  detailList.value.reduce((sum, item) => sum + Number(item.diffNums || 0), 0)
)

const formatQty = (val?: number | string) => {
  if (val === undefined || val === null || val === '') return ''
  const num = Number(val)
  return Number.isInteger(num) ? String(num) : num.toFixed(2)
}

const resetForm = () => {
  formData.value = {
    id: undefined,
    docnum: undefined,
    checkTime: undefined,
    profitNums: undefined,
    lossNums: undefined
  }
  detailList.value = []
}

const open = async (id: number) => {
  dialogVisible.value = true
  resetForm()
  formLoading.value = true
  try {
    const data = await StockCheckApi.getStockCheck(id)
    formData.value = {
      id: data.id,
      docnum: data.docnum,
      checkTime: data.checkTime,
      profitNums: data.profitNums,
      lossNums: data.lossNums
    }
    const lists = (data as any).stockStockcheckListList || (data as any).items || []
    detailList.value = (lists || []).map((item: any) => ({
      goodsCode: item.goodsCode || String(item.stockGoodsId || ''),
      goodsName: item.stockGoodsName || item.goodsName || '',
      spec: item.stockGoodsValueName || item.spec || '',
      unitName: item.unitName || '件',
      bookNums: item.bookNums,
      actualNums: item.actualNums,
      diffNums: item.diffNums,
      checkGoodsRemark: item.checkGoodsRemark
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

#stockcheck-print-area {
  color: #000;
  background: #fff;
  font-size: 13px;
  line-height: 1.4;
  font-family: 'SimSun', 'Songti SC', 'Microsoft YaHei', sans-serif;
}

#stockcheck-print-area .print-sheet__title {
  margin: 0 0 16px;
  text-align: center;
  font-size: 28px;
  font-weight: 700;
  letter-spacing: 8px;
}

#stockcheck-print-area .print-sheet__meta {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 10px;
  font-size: 14px;
}

#stockcheck-print-area .print-sheet__meta-left,
#stockcheck-print-area .print-sheet__meta-center {
  flex: 1;
}

#stockcheck-print-area .print-sheet__meta-right {
  flex: 1;
  text-align: right;
  line-height: 1.6;
}

#stockcheck-print-area .print-table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
  font-size: 13px;
}

#stockcheck-print-area .print-table th,
#stockcheck-print-area .print-table td {
  border: 1px solid #000;
  padding: 6px 4px;
  text-align: center;
  word-break: break-all;
  vertical-align: middle;
}

#stockcheck-print-area .print-table th {
  font-weight: 600;
}

#stockcheck-print-area .print-table .sum-label {
  text-align: left;
  padding-left: 12px;
}

#stockcheck-print-area .print-sheet__footer {
  display: flex;
  justify-content: space-between;
  margin-top: 28px;
  padding: 0 8px;
  font-size: 14px;
}
</style>
