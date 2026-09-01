<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="60%" top="8vh">
    <div v-loading="formLoading" class="audit-form">
      <div class="panel panel--mb">
        <div class="panel__title">盘库基本信息</div>
        <div class="panel__body">
          <el-form label-width="100px" size="default" class="basic-info">
            <el-row :gutter="16">
              <el-col :span="12">
                <el-form-item label="盘库单号:">
                  <span class="readonly-text">{{ formData.docnum || '-' }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="盘库日期:">
                  <span class="readonly-text">{{ checkDateText }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="盘盈合计:">
                  <span class="readonly-text text-profit">{{ formData.profitNums ?? 0 }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="盘亏合计:">
                  <span class="readonly-text text-loss">{{ formData.lossNums ?? 0 }}</span>
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
        <div class="panel__title">盘库商品详情</div>
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
            <el-table-column label="账面数量" align="center" prop="bookNums" width="90" />
            <el-table-column label="实盘数量" align="center" prop="actualNums" width="90" />
            <el-table-column label="盈亏" align="center" prop="diffNums" width="90" />
            <el-table-column label="单位" align="center" prop="unitName" width="70" />
            <el-table-column label="备注" align="center" prop="checkGoodsRemark" min-width="100" />
          </el-table>
        </div>
      </div>
      <div class="panel">
        <div class="panel__title">操作记录</div>
        <div class="panel__body panel__body--table">
          <Log type="STOCK_CHECK" :bizId="formData.id" />
        </div>
      </div>
    </div>
  </Dialog>
</template>

<script setup lang="ts">
import { formatDate } from '@/utils/formatTime'
import { StockCheckApi } from '@/api/stock/stockcheck'
import Log from '@/views/components/log/index.vue'

/** 商品盘库 审核/反审核 */
defineOptions({ name: 'StockCheckAudit' })

const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('详情')
const formLoading = ref(false)
const submitLoading = ref(false)

const formData = ref({
  id: undefined as number | undefined,
  docnum: undefined as string | undefined,
  checkTime: undefined as number | string | Date | undefined,
  profitNums: undefined as number | undefined,
  lossNums: undefined as number | undefined,
  remark: undefined as string | undefined,
  isAudit: undefined as number | boolean | undefined
})

interface DetailItem {
  goodsCode: string
  goodsName: string
  spec: string
  bookNums?: number
  actualNums?: number
  diffNums?: number
  unitName: string
  checkGoodsRemark?: string
}

const detailList = ref<DetailItem[]>([])

const checkDateText = computed(() => {
  if (!formData.value.checkTime) return '-'
  return formatDate(formData.value.checkTime as any, 'YYYY-MM-DD')
})

const resetForm = () => {
  formData.value = {
    id: undefined,
    docnum: undefined,
    checkTime: undefined,
    profitNums: undefined,
    lossNums: undefined,
    remark: undefined,
    isAudit: undefined
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
      lossNums: data.lossNums,
      remark: data.remark,
      isAudit: data.isAudit
    }
    const lists = (data as any).stockStockcheckListList || (data as any).items || []
    detailList.value = (lists || []).map((item: any) => ({
      goodsCode: item.goodsCode || String(item.stockGoodsId || ''),
      goodsName: item.stockGoodsName || item.goodsName || '',
      spec: item.stockGoodsValueName || item.spec || '',
      bookNums: item.bookNums,
      actualNums: item.actualNums,
      diffNums: item.diffNums,
      unitName: item.unitName || '件',
      checkGoodsRemark: item.checkGoodsRemark
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

.text-profit {
  color: #67c23a;
  font-weight: 600;
}

.text-loss {
  color: #f56c6c;
  font-weight: 600;
}
</style>
