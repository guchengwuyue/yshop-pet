<template>
  <el-row :gutter="20">
    <!-- 左侧分类树 -->
    <el-col :span="4" :xs="24">
      <ContentWrap class="h-1/1">
        <CateTree @node-click="handleDeptNodeClick" />
      </ContentWrap>
    </el-col>
    <el-col :span="20" :xs="24">
      <ContentWrap>
        <!-- 搜索工作栏 -->
        <el-form
          class="-mb-15px"
          :model="queryParams"
          ref="queryFormRef"
          :inline="true"
          label-width="68px"
        >
          <el-form-item label="商品名称" prop="storeName">
            <el-input
              v-model="queryParams.storeName"
              placeholder="请输入商品名称"
              clearable
              @keyup.enter="handleQuery"
              class="!w-240px"
            />
          </el-form-item>
          <el-form-item>
            <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
            <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
            <el-button type="primary" @click="handleStockIoDetail">
              <Icon icon="ep:list" class="mr-5px" /> 出入盘库明细
            </el-button>
          </el-form-item>
        </el-form>
      </ContentWrap>

      <!-- 列表 -->
      <ContentWrap>
        <el-table
          ref="tableRef"
          v-loading="loading"
          :data="list"
          :stripe="true"
          row-key="id"
          :show-overflow-tooltip="true"
          :expand-row-keys="expandRowKeys"
          @expand-change="handleExpandChange"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="45" align="center" />
          <el-table-column type="expand">
            <template #default="{ row }">
              <div class="sku-panel">
                <div class="sku-panel__tip">规格明细</div>
                <el-table
                  :data="getAttrValueList(row)"
                  size="small"
                  border
                  :show-overflow-tooltip="true"
                  empty-text="暂无规格明细"
                >
                  <el-table-column label="规格" align="center" min-width="120">
                    <template #default="{ row: sku }">
                      {{ getSkuName(sku) }}
                    </template>
                  </el-table-column>
                  <el-table-column label="商品代码" align="center" min-width="100">
                    <template #default="{ row: sku }">
                      {{ getSkuCode(sku) || '-' }}
                    </template>
                  </el-table-column>
                  <el-table-column label="条码" align="center" min-width="100">
                    <template #default="{ row: sku }">
                      {{ getSkuBarCode(sku) || '-' }}
                    </template>
                  </el-table-column>
                  <el-table-column label="单价" align="center" prop="price" width="80" />
                  <el-table-column label="库存" align="center" prop="stock" width="80" />
                </el-table>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="商品代码" align="center" prop="barCode" min-width="100">
            <template #default="{ row }">
              {{ row.barCode || row.id }}
            </template>
          </el-table-column>
          <el-table-column label="商品图片" align="center" width="100">
            <template #default="{ row }">
              <el-image
                v-if="row.image"
                style="width: 60px; height: 60px"
                :src="row.image"
                :preview-src-list="[row.image]"
                :initial-index="0"
                :z-index="900"
                :hide-on-click-modal="true"
                :preview-teleported="true"
                fit="cover"
              />
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column label="商品名称" align="center" prop="storeName" min-width="140" />
          <el-table-column label="规格" align="center" min-width="100">
            <template #default="{ row }">
              <el-tag size="small" :type="getAttrValueList(row).length > 1 ? 'warning' : 'info'">
                {{ getAttrValueList(row).length || 0 }} 个规格
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="单价" align="center" prop="price" width="80" />
          <el-table-column label="单位" align="center" prop="unitName" width="70" />
          <el-table-column label="库存" align="center" prop="stock" width="80" />
          <el-table-column
            label="添加时间"
            align="center"
            prop="createTime"
            width="170"
            :formatter="dateFormatter"
          />
        </el-table>
        <!-- 分页 -->
        <Pagination
          :total="total"
          v-model:page="queryParams.pageNo"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
        />
      </ContentWrap>
    </el-col>
  </el-row>

  <!-- 出入盘库明细 -->
  <StockIoDetail ref="detailRef" />
</template>

<script setup lang="ts" name="StockSearch">
import { dateFormatter } from '@/utils/formatTime'
import * as StoreProductApi from '@/api/mall/product/product'
import type { StoreProductVO, StoreProductAttrValueVO } from '@/api/mall/product/product'
import CateTree from '@/views/mall/product/storeProduct/CateTree.vue'
import StockIoDetail from './detail.vue'

const message = useMessage()
const loading = ref(true)
const total = ref(0)
const list = ref<StoreProductVO[]>([])
const expandRowKeys = ref<(string | number)[]>([])
const selectedRows = ref<StoreProductVO[]>([])
const tableRef = ref()
const detailRef = ref()
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  storeName: null as string | null,
  isPostage: null,
  stock: 1,
  cateId: null as number | null,
  shopName: null
})
const queryFormRef = ref()

const getAttrValueList = (row: StoreProductVO | any): StoreProductAttrValueVO[] => {
  return row?.attrValueList || row?.attr_value_list || []
}

const getSkuName = (sku: StoreProductAttrValueVO | any) => {
  return sku?.suk || sku?.sku || sku?.attrValue || '-'
}

const getSkuCode = (sku: StoreProductAttrValueVO | any) => {
  return sku?.goodCode || sku?.good_code || sku?.barCode || sku?.bar_code || ''
}

const getSkuBarCode = (sku: StoreProductAttrValueVO | any) => {
  return sku?.barCode || sku?.bar_code || ''
}

const handleExpandChange = (_row: StoreProductVO, expandedRows: StoreProductVO[]) => {
  expandRowKeys.value = expandedRows.map((item) => item.id)
}

/** 单选：一次只能选择一个商品 */
const handleSelectionChange = (val: StoreProductVO[]) => {
  if (val.length > 1) {
    const lastSelected = val[val.length - 1]
    tableRef.value?.clearSelection()
    tableRef.value?.toggleRowSelection(lastSelected, true)
    selectedRows.value = [lastSelected]
  } else {
    selectedRows.value = val
  }
}

/** 出入盘库明细 */
const handleStockIoDetail = () => {
  if (!selectedRows.value.length) {
    message.warning('请先选择一个商品')
    return
  }
  const row = selectedRows.value[0]
  detailRef.value?.open(row.id, row.storeName)
}

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await StoreProductApi.getStoreProductPage(queryParams)
    list.value = data.list || []
    total.value = data.total || 0
    expandRowKeys.value = []
    selectedRows.value = []
  } finally {
    loading.value = false
  }
}

/** 分类点击 */
const handleDeptNodeClick = async (row) => {
  queryParams.cateId = row.id
  await getList()
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  queryParams.cateId = null
  handleQuery()
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>

<style scoped lang="scss">
.sku-panel {
  padding: 8px 12px 12px 48px;
  background: #fafafa;

  &__tip {
    margin-bottom: 8px;
    font-size: 12px;
    color: var(--el-color-warning);
  }
}
</style>
