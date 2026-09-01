<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="60%" top="8vh">
    <div v-loading="formLoading" class="audit-form">
      <div class="panel panel--mb">
        <div class="panel__title">出库基本信息</div>
        <div class="panel__body">
          <el-form label-width="100px" size="default" class="basic-info">
            <el-row :gutter="16">
              <el-col :span="12">
                <el-form-item label="出库单号:">
                  <span class="readonly-text">{{ formData.docnum || '-' }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="客户:">
                  <span class="readonly-text">{{ customerName }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="出库日期:">
                  <span class="readonly-text">{{ outboundDateText }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="出库总金额:">
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
        <div class="panel__title">出库商品详情</div>
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
            <el-table-column label="出库数量" align="center" prop="stockOutNums" width="90" />
            <el-table-column label="单位" align="center" prop="unitName" width="70" />
            <el-table-column label="出库单价" align="center" prop="outboundPrice" width="100" />
            <el-table-column label="出库金额" align="center" prop="amount" width="100" />
            <el-table-column label="备注" align="center" prop="stockOutGoodsRemark" min-width="100" />
          </el-table>
        </div>
      </div>
    </div>
    <template #footer>
      <el-button
        type="primary"
        :loading="submitLoading"
        :disabled="formLoading"
        @click="submitAudit"
      >
        {{ auditType === 'unaudit' ? '反审核' : '审核' }}
      </el-button>
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import { formatDate } from '@/utils/formatTime'
import { StockoutApi } from '@/api/stock/stockstockout'

/** 商品出库 审核/反审核 */
defineOptions({ name: 'StockoutAudit' })

const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('审核')
const formLoading = ref(false)
const submitLoading = ref(false)
/** audit | unaudit */
const auditType = ref<'audit' | 'unaudit'>('audit')

const formData = ref({
  id: undefined as number | undefined,
  docnum: undefined as string | undefined,
  stockCustomerId: undefined as number | undefined,
  stockCustomerName: undefined as string | undefined,
  outboundTime: undefined as number | string | Date | undefined,
  totalOutAmount: undefined as number | undefined,
  remark: undefined as string | undefined,
  isAudit: undefined as number | boolean | undefined
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

const customerName = computed(() => formData.value.stockCustomerName || '-')

const outboundDateText = computed(() => {
  if (!formData.value.outboundTime) return '-'
  return formatDate(formData.value.outboundTime as any, 'YYYY-MM-DD')
})

const amountText = computed(() => {
  const amount = formData.value.totalOutAmount
  if (amount === undefined || amount === null) return '-'
  return Number(amount).toFixed(2)
})

const resetForm = () => {
  formData.value = {
    id: undefined,
    docnum: undefined,
    stockCustomerId: undefined,
    stockCustomerName: undefined,
    outboundTime: undefined,
    totalOutAmount: undefined,
    remark: undefined,
    isAudit: undefined
  }
  detailList.value = []
}

/** 打开弹窗：type = audit | unaudit */
const open = async (type: 'audit' | 'unaudit', id: number) => {
  dialogVisible.value = true
  auditType.value = type
  dialogTitle.value = type === 'unaudit' ? '反审核' : '审核'
  resetForm()
  formLoading.value = true
  try {
    const data = await StockoutApi.getStockout(id)
    formData.value = {
      id: data.id,
      docnum: data.docnum,
      stockCustomerId: data.stockCustomerId,
      stockCustomerName: data.stockCustomerName,
      outboundTime: data.outboundTime,
      totalOutAmount: data.totalOutAmount,
      remark: data.remark,
      isAudit: data.isAudit
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

const emit = defineEmits(['success'])

const submitAudit = async () => {
  if (!formData.value.id) return
  submitLoading.value = true
  try {
    await StockoutApi.auditStockout(formData.value.id, auditType.value)
    message.success(auditType.value === 'unaudit' ? '反审核成功' : '审核成功')
    dialogVisible.value = false
    emit('success')
  } finally {
    submitLoading.value = false
  }
}
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
