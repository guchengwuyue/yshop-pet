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
      <el-form-item label="单据编号" prop="docNumber">
        <el-input
          v-model="queryParams.docNumber"
          placeholder="请输入单据编号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="客户" prop="customerName">
        <el-input
          v-model="queryParams.customerName"
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
      <el-form-item label="商品代码" prop="goodsCode">
        <el-input
          v-model="queryParams.goodsCode"
          placeholder="请输入商品代码"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="商品名称" prop="stockGoodsName">
        <el-input
          v-model="queryParams.stockGoodsName"
          placeholder="请输入商品名称"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
        <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 刷新</el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="单据编号" align="center" prop="docNumber" width="180px" />
      <el-table-column label="客户" align="center" prop="customerName" width="120px" />
      <el-table-column
        label="出库时间"
        align="center"
        prop="outboundTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column label="商品编码" align="center" prop="goodsCode" width="150px" />
      <el-table-column label="商品名称" align="center" prop="stockGoodsName" />
      <el-table-column label="商品规格" align="center" prop="stockGoodsValueName" />
      <el-table-column label="商品单位" align="center" prop="unitName" />
      <el-table-column label="出库单价" align="center" prop="outboundPrice" />
      <el-table-column label="出库数量" align="center" prop="stockOutNums" />
      <el-table-column label="出库金额" align="center" prop="amount">
        <template #default="scope">
          <span>{{
            scope.row.amount ??
            Number((Number(scope.row.outboundPrice || 0) * Number(scope.row.stockOutNums || 0)).toFixed(2))
          }}</span>
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
</template>

<script setup lang="ts">
import { dateFormatter } from '@/utils/formatTime'
import { StockoutListApi, StockoutListVO } from '@/api/stock/stockstockoutlist'

/** 商品出库详情 列表 */
defineOptions({ name: 'StockStockoutList' })

const loading = ref(true)
const list = ref<StockoutListVO[]>([])
const total = ref(0)
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  docNumber: undefined,
  customerName: undefined,
  outboundTime: undefined as string[] | undefined,
  goodsCode: undefined,
  stockGoodsName: undefined,
  amount: undefined,
  stockOutGoodsRemark: undefined,
  createTime: []
})
const queryFormRef = ref()

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await StockoutListApi.getStockoutListPage(queryParams)
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

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
