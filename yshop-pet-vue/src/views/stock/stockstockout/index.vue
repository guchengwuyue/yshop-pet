<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="单据编号" prop="docnum">
        <el-input
          v-model="queryParams.docnum"
          placeholder="请输入单据编号或者扫码枪扫码"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="客户" prop="stockCustomerName">
        <el-input
          v-model="queryParams.stockCustomerName"
          placeholder="请输入客户"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="出库时间" prop="outboundTime">
        <el-date-picker
          v-model="queryParams.outboundTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="审核状态" prop="isAudit">
        <el-select
          v-model="queryParams.isAudit"
          placeholder="请选择审核状态"
          clearable
          class="!w-240px"
        >
          <el-option label="未审核" :value="0" />
          <el-option label="已审核" :value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
        <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
        <el-button
          type="primary"
          plain
          @click="openForm('create')"
          v-hasPermi="['stock:stockout:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button
          type="danger"
          plain
          @click="openAudit"
          v-hasPermi="['stock:stockout:update']"
        >
          <Icon icon="ep:circle-check" class="mr-5px" /> 审核/反审核
        </el-button>
           <!-- 打印按钮 -->
        <el-button type="primary" @click="handlePrint"><Icon icon="ep:printer" class="mr-5px" /> 打印</el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table
      v-loading="loading"
      :data="list"
      :stripe="true"
      :show-overflow-tooltip="true"
      @selection-change="handleSelectionChange"
      ref="tableRef"
      row-key="id"
    >
      <el-table-column type="expand">
        <template #default="scope">
          <div class="stockout-detail-wrap">
            <el-table
              :data="scope.row.stockStockoutListList || []"
              border
              size="small"
              :header-cell-style="{ background: '#f5f7fa', color: '#606266' }"
            >
              <el-table-column label="商品名称" align="center" prop="stockGoodsName" min-width="120" />
              <el-table-column label="规格型号" align="center" prop="stockGoodsValueName" min-width="100" />
              <el-table-column label="数量" align="center" prop="stockOutNums" width="90" />
              <el-table-column label="单位" align="center" prop="unitName" width="80" />
              <el-table-column label="出库单价" align="center" prop="outboundPrice" width="100" />
              <el-table-column label="金额" align="center" prop="amount" width="100" />
              <el-table-column
                label="备注"
                align="center"
                prop="stockOutGoodsRemark"
                min-width="120"
              />
            </el-table>
          </div>
        </template>
      </el-table-column>
      <el-table-column type="selection" width="45" align="center" />
      <el-table-column label="单据编号" align="center" prop="docnum" width="200">
        <template #default="scope">
          <el-link type="primary" @click="openDetail(scope.row.id)">{{ scope.row.docnum }}</el-link>
        </template>
      </el-table-column>
      <el-table-column label="客户" align="center" min-width="140">
        <template #default="scope">
          {{ scope.row.stockCustomerName || scope.row.stockCustomerId }}
        </template>
      </el-table-column>
      <el-table-column
        label="出库时间"
        align="center"
        prop="outboundTime"
        :formatter="dateFormatter"
      />
      <el-table-column label="总金额" align="center" prop="totalOutAmount"  />
      <el-table-column label="审核状态" align="center" prop="isAudit" >
        <template #default="scope">
          <span class="status-dot" :class="isAudited(scope.row.isAudit) ? 'success' : 'danger'">
            {{ isAudited(scope.row.isAudit) ? '已审核' : '未审核' }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="审核时间" align="center" prop="auditTime" >
        <template #default="{ row }">
          <span>{{ row.auditTime ? formatDate(row.auditTime) : '--' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="120" fixed="right">
        <template #default="scope">
          <el-button
            type="success"
            :icon="Edit"
            circle
            size="small"
            @click="openForm('update', scope.row)"
            v-hasPermi="['stock:stockout:update']"
          />
          <el-button
            type="danger"
            :icon="Delete"
            circle
            size="small"
            @click="handleDelete(scope.row)"
            v-hasPermi="['stock:stockout:delete']"
          />
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>

  <!-- 表单弹窗：添加/修改 -->
  <StockoutForm ref="formRef" @success="getList" />
  <!-- 审核/反审核弹窗 -->
  <StockoutAudit ref="auditRef" @success="getList" />
  <!-- 打印弹窗 -->
  <StockoutPrinter ref="printerRef" />
  <StockoutDetail ref="detailRef" />
</template>

<script setup lang="ts">
import { Edit, Delete } from '@element-plus/icons-vue'
import { dateFormatter, formatDate } from '@/utils/formatTime'
import { StockoutApi, StockoutVO } from '@/api/stock/stockstockout'
import StockoutForm from './StockoutForm.vue'
import StockoutAudit from './audit.vue'
import StockoutPrinter from './printer.vue'
import StockoutDetail from './detail.vue'

/** 商品出库 列表 */
defineOptions({ name: 'StockStockout' })

const message = useMessage()
const { t } = useI18n()

const loading = ref(true)
const list = ref<StockoutVO[]>([])
const total = ref(0)
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  docnum: undefined,
  stockCustomerName: undefined,
  outboundTime: [],
  stockOutTotalNums: undefined,
  totalOutAmount: undefined,
  remark: undefined,
  isAudit: undefined,
  auditTime: [],
  createTime: []
})
const queryFormRef = ref()

const isAudited = (val: number | boolean | undefined) => val === 1 || val === true

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await StockoutApi.getStockoutPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

const detailRef = ref()
const openDetail = (id) => {
  detailRef.value.open(id)
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, row) => {
  if (row?.id && isAudited(row.isAudit)) {
    message.warning('该出库单已审核，不可以修改')
    return
  }
  formRef.value.open(type, row?.id)
}

/** 审核/反审核操作 */
const auditRef = ref()
const openAudit = () => {
  if (!selectedRows.value.length) {
    message.warning('请先选择一条出库单')
    return
  }
  const row = selectedRows.value[0]
  const type = isAudited(row.isAudit) ? 'unaudit' : 'audit'
  auditRef.value.open(type, row.id)
}

/** 打印操作 */
const printerRef = ref()
const handlePrint = () => {
  if (!selectedRows.value.length) {
    message.warning('请先选择一条出库单')
    return
  }
  printerRef.value.open(selectedRows.value[0].id)
}

const selectedRows = ref<StockoutVO[]>([])
const tableRef = ref()
const handleSelectionChange = (val: StockoutVO[]) => {
  if (val.length > 1) {
    const lastSelected = val[val.length - 1]
    tableRef.value.clearSelection()
    tableRef.value.toggleRowSelection(lastSelected, true)
    selectedRows.value = [lastSelected]
  } else {
    selectedRows.value = val
  }
}

/** 删除按钮操作 */
const handleDelete = async (row) => {
  try {
    if (row?.id && isAudited(row.isAudit)) {
      message.warning('该入库单已审核，不可以删除')
      return
    }
    await message.delConfirm()
    await StockoutApi.deleteStockout(row.id)
    message.success(t('common.delSuccess'))
    await getList()
  } catch {}
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>

<style scoped>
.stockout-detail-wrap {
  padding: 8px 48px 12px;
}

.status-dot {
  position: relative;
  padding-left: 12px;
}

.status-dot::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  transform: translateY(-50%);
}

.status-dot.danger::before {
  background-color: var(--el-color-danger);
}

.status-dot.success::before {
  background-color: var(--el-color-success);
}
</style>
