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
      <el-form-item label="供应商" prop="supplierName">
        <el-input
          v-model="queryParams.supplierName"
          placeholder="请输入供应商"
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
      <el-table-column label="供应商" align="center" prop="supplierName" width="120px" />
      <el-table-column
        label="入库时间"
        align="center"
        prop="inboundTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column label="商品编码" align="center" prop="goodsCode" width="150px" />
      <el-table-column label="商品名称" align="center" prop="stockGoodsName" />
      <el-table-column label="商品规格" align="center" prop="stockGoodsValueName" />
      <el-table-column label="商品单位" align="center" prop="unitName" />
      <el-table-column label="入库单价" align="center" prop="amount" />
      <el-table-column label="入库数量" align="center" prop="stockinNums" />
      <el-table-column label="实收金额" align="center" prop="charged">
        <template #default="scope">
          <!-- 单价*数量 -->
          <span>{{ scope.row.amount * scope.row.stockinNums }}</span>
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
import { StockinListApi, StockinListVO } from '@/api/stock/stockstockinlist'

/** 商品入库详情 列表 */
defineOptions({ name: 'StockStockinList' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<StockinListVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  docNumber: undefined,
  supplierName: undefined,
  inboundTime: undefined,
  goodsCode: undefined,
  stockGoodsName: undefined,
  amount: undefined,
  stockinGoodsRemark: undefined,
  charged: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await StockinListApi.getStockinListPage(queryParams)
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