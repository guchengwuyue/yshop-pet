<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="90%" top="5vh">
    <div class="io-detail">
      <div class="io-detail__toolbar">
        <el-input
          v-model="queryParams.keyword"
          placeholder="搜索单据编号/商品代码/名称/规格/类型"
          clearable
          class="!w-280px"
          @keyup.enter="handleQuery"
        >
          <template #prefix>
            <Icon icon="ep:search" />
          </template>
        </el-input>
        <el-button @click="handleQuery">
          <Icon icon="ep:search" class="mr-5px" /> 搜索
        </el-button>
      </div>

      <el-table
        v-loading="loading"
        :data="list"
        :stripe="true"
        border
        row-key="rowKey"
        :show-overflow-tooltip="true"
        show-summary
        :summary-method="getSummaries"
        max-height="560"
      >
        <el-table-column type="selection" width="45" align="center" />
        <el-table-column label="业务类型" align="center" prop="ioType" width="90">
          <template #default="{ row }">
            <el-tag :type="getIoTypeTag(row.ioType)" size="small">
              {{ row.ioType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="单据编号" align="center" prop="docnum" min-width="130">
          <template #default="{ row }">
            {{ row.docnum || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="商品代码" align="center" prop="goodsCode" min-width="100" />
        <el-table-column label="商品名称" align="center" prop="stockGoodsName" min-width="120" />
        <el-table-column
          label="规格型号"
          align="center"
          prop="stockGoodsValueName"
          min-width="100"
        />
        <el-table-column label="单位" align="center" prop="unitName" width="70" />
        <el-table-column label="单价" align="center" prop="unitPrice" width="100">
          <template #default="{ row }">
            {{ formatMoney(row.unitPrice) }}
          </template>
        </el-table-column>
        <el-table-column label="入库数量" align="center" prop="stockinNums" width="90">
          <template #default="{ row }">
            {{ emptyIfNull(row.stockinNums) }}
          </template>
        </el-table-column>
        <el-table-column label="入库总价" align="center" prop="stockinAmount" width="100">
          <template #default="{ row }">
            {{ formatMoney(row.stockinAmount) }}
          </template>
        </el-table-column>
        <el-table-column label="出库数量" align="center" prop="stockOutNums" width="90">
          <template #default="{ row }">
            {{ emptyIfNull(row.stockOutNums) }}
          </template>
        </el-table-column>
        <el-table-column label="出库总价" align="center" prop="stockOutAmount" width="100">
          <template #default="{ row }">
            {{ formatMoney(row.stockOutAmount) }}
          </template>
        </el-table-column>
        <el-table-column label="账面数量" align="center" prop="bookNums" width="90">
          <template #default="{ row }">
            {{ emptyIfNull(row.bookNums) }}
          </template>
        </el-table-column>
        <el-table-column label="实盘数量" align="center" prop="actualNums" width="90">
          <template #default="{ row }">
            {{ emptyIfNull(row.actualNums) }}
          </template>
        </el-table-column>
        <el-table-column label="盈亏" align="center" prop="diffNums" width="90">
          <template #default="{ row }">
            <span
              v-if="row.diffNums !== null && row.diffNums !== undefined"
              :class="{
                'text-success': Number(row.diffNums) > 0,
                'text-danger': Number(row.diffNums) < 0
              }"
            >
              {{ row.diffNums }}
            </span>
          </template>
        </el-table-column>
        <el-table-column
          label="出入盘时间"
          align="center"
          prop="ioTime"
          width="120"
          :formatter="dateFormatter2"
        />
      </el-table>

      <div class="io-detail__footer">
        <span class="io-detail__range">
          显示第 {{ rangeStart }} 到第 {{ rangeEnd }} 条记录，总共 {{ total }} 条记录
        </span>
        <Pagination
          :total="total"
          v-model:page="queryParams.pageNo"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
        />
      </div>
    </div>
  </Dialog>
</template>

<script setup lang="ts">
import { dateFormatter2 } from '@/utils/formatTime'
import { StockIoDetailApi, StockIoDetailVO } from '@/api/stock/stockiodetail'
import type { TableColumnCtx } from 'element-plus'

defineOptions({ name: 'StockIoDetail' })

const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('出入盘库明细')
const loading = ref(false)
const list = ref<StockIoDetailVO[]>([])
const total = ref(0)

const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  stockGoodsId: undefined as number | undefined,
  keyword: undefined as string | undefined
})

const rangeStart = computed(() => {
  if (!total.value) return 0
  return (queryParams.pageNo - 1) * queryParams.pageSize + 1
})

const rangeEnd = computed(() => {
  if (!total.value) return 0
  return Math.min(queryParams.pageNo * queryParams.pageSize, total.value)
})

const getIoTypeTag = (ioType: string) => {
  if (ioType === '入库') return 'success'
  if (ioType === '出库') return 'warning'
  if (ioType === '盘库') return 'info'
  return ''
}

const emptyIfNull = (val: number | null | undefined) => {
  if (val === null || val === undefined || val === ('' as any)) return ''
  return val
}

const formatMoney = (val: number | null | undefined) => {
  if (val === null || val === undefined || val === ('' as any)) return ''
  return Number(val).toFixed(2)
}

const getSummaries = (param: { columns: TableColumnCtx<StockIoDetailVO>[]; data: StockIoDetailVO[] }) => {
  const { columns, data } = param
  const sums: string[] = []
  columns.forEach((column, index) => {
    if (index === 0) {
      sums[index] = '合计'
      return
    }
    const prop = column.property
    if (
      prop === 'stockinNums' ||
      prop === 'stockOutNums' ||
      prop === 'bookNums' ||
      prop === 'actualNums' ||
      prop === 'diffNums'
    ) {
      const totalNum = data.reduce((sum, row) => sum + Number((row as any)[prop] || 0), 0)
      sums[index] = String(totalNum)
      return
    }
    if (prop === 'stockinAmount' || prop === 'stockOutAmount') {
      const totalAmount = data.reduce((sum, row) => sum + Number((row as any)[prop] || 0), 0)
      sums[index] = totalAmount.toFixed(2)
      return
    }
    sums[index] = ''
  })
  return sums
}

const getList = async () => {
  if (!queryParams.stockGoodsId) return
  loading.value = true
  try {
    const data = await StockIoDetailApi.getStockIoDetailPage(queryParams)
    list.value = data.list || []
    total.value = data.total || 0
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

/** 打开弹窗 */
const open = async (stockGoodsId: number, storeName?: string) => {
  if (!stockGoodsId) {
    message.warning('请先选择一个商品')
    return
  }
  dialogVisible.value = true
  dialogTitle.value = storeName ? `出入盘库明细 - ${storeName}` : '出入盘库明细'
  queryParams.stockGoodsId = stockGoodsId
  queryParams.pageNo = 1
  queryParams.keyword = undefined
  await getList()
}

defineExpose({ open })
</script>

<style scoped lang="scss">
.io-detail {
  &__toolbar {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    gap: 8px;
    margin-bottom: 12px;
  }

  &__footer {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-top: 12px;
    flex-wrap: wrap;
    gap: 8px;
  }

  &__range {
    font-size: 13px;
    color: var(--el-text-color-secondary);
  }
}

.text-success {
  color: var(--el-color-success);
}

.text-danger {
  color: var(--el-color-danger);
}

:deep(.el-pagination) {
  margin-top: 0;
}
</style>
