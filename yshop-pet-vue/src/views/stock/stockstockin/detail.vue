<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="60%" top="8vh">
    <div v-loading="formLoading" class="audit-form">
      <div class="panel panel--mb">
        <div class="panel__title">入库基本信息</div>
        <div class="panel__body">
          <el-form label-width="100px" size="default" class="basic-info">
            <el-row :gutter="16">
              <el-col :span="12">
                <el-form-item label="入库单号:">
                  <span class="readonly-text">{{ formData.docnum || '-' }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="供应商:">
                  <span class="readonly-text">{{ supplierName }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="入库日期:">
                  <span class="readonly-text">{{ inboundDateText }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="入库总金额:">
                  <span class="readonly-text">{{ amountText }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="备注:">
                  <span class="readonly-text">{{ formData.remark || '-' }}</span>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
      </div>

      <div class="panel">
        <div class="panel__title">入库商品详情</div>
        <div class="panel__body panel__body--table">
          <el-table
            :data="detailList"
            :stripe="true"
            border
            max-height="360"
            :show-overflow-tooltip="true"
            :header-cell-style="{ background: '#f5f7fa', color: '#606266' }"
          >
            <el-table-column label="商品代码" align="center" prop="goodsCode" min-width="90" />
            <el-table-column label="商品名称" align="center" prop="goodsName" min-width="110" />
            <el-table-column label="规格" align="center" prop="spec" min-width="100" />
            <el-table-column label="入库数量" align="center" prop="stockinNums" width="90" />
            <el-table-column label="单位" align="center" prop="unitName" width="70" />
            <el-table-column label="入库单价" align="center" prop="inboundPrice" width="100" />
            <el-table-column label="实收金额" align="center" prop="amount" width="100" />
            <el-table-column label="备注" align="center" prop="stockinGoodsRemark" min-width="100" />
          </el-table>
        </div>
      </div>

      <div class="panel">
        <div class="panel__title">操作记录</div>
        <div class="panel__body panel__body--table">
          <Log type="STOCK_IN" :bizId="formData.id" />
        </div>
      </div>
    </div>
  </Dialog>
</template>

<script setup lang="ts">
import { formatDate } from '@/utils/formatTime'
import { StockinApi } from '@/api/stock/stockstockin'
import Log from '@/views/components/log/index.vue'
/** 商品入库 审核/反审核 */
defineOptions({ name: 'StockinAudit' })

const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('详情')
const formLoading = ref(false)
const submitLoading = ref(false)

const formData = ref({
  id: undefined as number | undefined,
  docnum: undefined as string | undefined,
  stockSupplierId: undefined as number | undefined,
  stockSupplierName: undefined as string | undefined,
  supplierName: undefined as string | undefined,
  inboundTime: undefined as number | string | Date | undefined,
  totalAmount: undefined as number | undefined,
  remark: undefined as string | undefined,
  isAudit: undefined as number | boolean | undefined
})

interface DetailItem {
  goodsCode: string
  goodsName: string
  spec: string
  stockinNums?: number
  unitName: string
  inboundPrice?: number
  amount?: number
  stockinGoodsRemark?: string
}

const detailList = ref<DetailItem[]>([])

const supplierName = computed(
  () => formData.value.stockSupplierName || formData.value.supplierName || '-'
)

const inboundDateText = computed(() => {
  if (!formData.value.inboundTime) return '-'
  return formatDate(formData.value.inboundTime as any, 'YYYY-MM-DD')
})

const amountText = computed(() => {
  const amount = formData.value.totalAmount
  if (amount === undefined || amount === null) return '-'
  return Number(amount).toFixed(2)
})

const resetForm = () => {
  formData.value = {
    id: undefined,
    docnum: undefined,
    stockSupplierId: undefined,
    stockSupplierName: undefined,
    supplierName: undefined,
    inboundTime: undefined,
    totalAmount: undefined,
    remark: undefined,
    isAudit: undefined
  }
  detailList.value = []
}

/** 打开弹窗：type = audit | unaudit */
const open = async (id: number) => {
  dialogVisible.value = true
  resetForm()
  formLoading.value = true
  try {
    const data = await StockinApi.getStockin(id)
    formData.value = {
      id: data.id,
      docnum: data.docnum,
      stockSupplierId: data.stockSupplierId,
      stockSupplierName: data.stockSupplierName,
      supplierName: data.supplierName,
      inboundTime: data.inboundTime,
      totalAmount: data.totalAmount,
      remark: data.remark,
      isAudit: data.isAudit
    }
    const lists = (data as any).stockStockinListList || (data as any).items || []
    detailList.value = (lists || []).map((item: any) => ({
      goodsCode: item.goodsCode || String(item.stockGoodsId || ''),
      goodsName: item.stockGoodsName || item.goodsName || '',
      spec: item.stockGoodsValueName || item.spec || '',
      stockinNums: item.stockinNums,
      unitName: item.unitName || '件',
      inboundPrice: item.inboundPrice,
      amount: item.amount,
      stockinGoodsRemark: item.stockinGoodsRemark
    }))
  } finally {
    formLoading.value = false
  }
}
defineExpose({ open })

</script>

<style scoped lang="scss">
.audit-form {
  min-height: 200px;
}

.panel {
  border: 1px solid var(--el-border-color-lighter);
  border-radius: 4px;
  background: var(--el-bg-color);
  overflow: hidden;

  &--mb {
    margin-bottom: 12px;
  }

  &__title {
    padding: 8px 12px;
    background: #f5f7fa;
    border-bottom: 1px solid var(--el-border-color-lighter);
    font-size: 14px;
    font-weight: 500;
    color: var(--el-text-color-primary);
  }

  &__body {
    padding: 12px 12px 4px;

    &--table {
      padding: 12px;
    }
  }
}

.basic-info {
  :deep(.el-form-item) {
    margin-bottom: 12px;
  }
}

.readonly-text {
  color: var(--el-text-color-regular);
  line-height: 32px;
}
</style>
