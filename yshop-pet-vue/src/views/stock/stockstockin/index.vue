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
          placeholder="请输入单据编号或者扫码搜索"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="供应商" prop="stockSupplierId">
        <el-input
          v-model="queryParams.stockSupplierId"
          placeholder="请输入供应商ID"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="入库时间" prop="inboundTime">
        <el-date-picker
          v-model="queryParams.inboundTime"
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
          v-hasPermi="['stock:stockin:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button
          type="danger"
          plain
          @click="openAudit"
          v-hasPermi="['stock:stockin:update']"
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
          <div class="stockin-detail-wrap">
            <el-table
              :data="scope.row.stockStockinListList || []"
              border
              size="small"
              :header-cell-style="{ background: '#f5f7fa', color: '#606266' }"
            >
              <el-table-column label="商品名称" align="center" prop="stockGoodsName" min-width="120" />
              <el-table-column label="规格型号" align="center" prop="stockGoodsValueName" min-width="100" />
              <el-table-column label="数量" align="center" prop="stockinNums" width="90" />
              <el-table-column label="单位" align="center" prop="unitName" width="80" />
              <el-table-column label="入库单价" align="center" prop="inboundPrice" width="100" />
              <el-table-column label="金额" align="center" prop="amount" width="100" />
              <el-table-column
                label="备注"
                align="center"
                prop="stockinGoodsRemark"
                min-width="120"
              />
            </el-table>
          </div>
        </template>
      </el-table-column>
      <el-table-column type="selection" width="45" align="center" />
      <!-- <el-table-column label="ID" align="center" prop="id" width="70" /> -->
      <el-table-column label="单据编号" align="center" prop="docnum" width="200">
        <template #default="scope">
          <el-link type="primary" @click="openDetail(scope.row.id)">{{ scope.row.docnum }}</el-link>
        </template>
      </el-table-column>
      <el-table-column label="供应商" align="center" min-width="140">
        <template #default="scope">
          {{ scope.row.stockSupplierName || scope.row.supplierName || scope.row.stockSupplierId }}
        </template>
      </el-table-column>
      <el-table-column
        label="入库时间"
        align="center"
        prop="inboundTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column label="总金额" align="center" prop="totalAmount" />
      <el-table-column label="审核状态" align="center" prop="isAudit" >
        <template #default="scope">
          <span class="status-dot" :class="isAudited(scope.row.isAudit) ? 'success' : 'danger'">
            {{ isAudited(scope.row.isAudit) ? '已审核' : '未审核' }}
          </span>
        </template>
      </el-table-column>
      <el-table-column
        label="审核时间"
        align="center"
        prop="auditTime"
      >
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
            v-hasPermi="['stock:stockin:update']"
          />
          <el-button
            type="danger"
            :icon="Delete"
            circle
            size="small"
            @click="handleDelete(scope.row)"
            v-hasPermi="['stock:stockin:delete']"
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
  <StockinForm ref="formRef" @success="getList" />
  <!-- 审核/反审核弹窗 -->
  <StockinAudit ref="auditRef" @success="getList" />
  <!-- 打印弹窗 -->
  <StockinPrinter ref="printerRef" />
  <StockinDetail ref="detailRef" />
</template>

<script setup lang="ts">
import { Edit, Delete } from '@element-plus/icons-vue'
import { dateFormatter,formatDate } from '@/utils/formatTime'
import { StockinApi, StockinVO } from '@/api/stock/stockstockin'
import StockinForm from './StockinForm.vue'
import StockinAudit from './audit.vue'
import StockinPrinter from './printer.vue'
import StockinDetail from './detail.vue'

/** 商品入库 列表 */
defineOptions({ name: 'StockStockin' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<StockinVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  docnum: undefined,
  stockSupplierId: undefined,
  inboundTime: [],
  stockinTotalNums: undefined,
  totalAmount: undefined,
  remark: undefined,
  isAudit: undefined,
  auditTime: [],
  isSettle: undefined,
  settleTime: [],
  createTime: []
})
const queryFormRef = ref() // 搜索的表单

const isAudited = (val: number | boolean | undefined) => val === 1 || val === true

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await StockinApi.getStockinPage(queryParams)
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

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, row) => {
  //点编辑时候判断如果已经审核了 不可以修改
  if (row?.id && isAudited(row.isAudit)) {
    message.warning('该入库单已审核，不可以修改')
    return
  }
  formRef.value.open(type, row?.id)
}

/** 审核/反审核操作 */
const auditRef = ref()
const openAudit = () => {
  if (!selectedRows.value.length) {
    message.warning('请先选择一条入库单')
    return
  }
  const row = selectedRows.value[0]
  const type = isAudited(row.isAudit) ? 'unaudit' : 'audit'
  auditRef.value.open(type, row.id)
}

const detailRef = ref()
const openDetail = (id) => {
  detailRef.value.open(id)
}


/** 打印操作 */
const printerRef = ref()
const handlePrint = () => {
  if (!selectedRows.value.length) {
    message.warning('请先选择一条入库单')
    return
  }
  printerRef.value.open(selectedRows.value[0].id)
}

const selectedRows = ref<StockinVO[]>([])
const tableRef = ref()
const handleSelectionChange = (val: StockinVO[]) => {
  if (val.length > 1) {
    // 获取最后点击的那一行（通常是数组最后一个）
    const lastSelected = val[val.length - 1]

    // 清除所有选中
    tableRef.value.clearSelection()

    // 重新选中最后点击的那一行
    tableRef.value.toggleRowSelection(lastSelected, true)

    // 更新本地状态
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
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await StockinApi.deleteStockin(row.id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>

<style scoped>
.stockin-detail-wrap {
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
