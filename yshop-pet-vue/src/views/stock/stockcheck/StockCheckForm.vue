<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="92%" top="5vh">
    <div v-loading="formLoading" class="stockcheck-form">
      <el-row :gutter="12" class="stockcheck-form__row">
        <!-- 左侧：选择盘库商品 -->
        <el-col :span="12" class="stockcheck-form__col">
          <div class="panel">
            <div class="panel__title">选择盘库商品</div>
            <div class="panel__body">
              <el-form
                ref="itemFormRef"
                :model="itemForm"
                :rules="itemFormRules"
                label-width="90px"
                class="item-entry"
                size="default"
              >
                <el-row :gutter="10">
                  <el-col :span="12">
                    <el-form-item label="商品条码:" prop="barCode">
                      <el-input
                        v-model="itemForm.barCode"
                        placeholder="请输入商品条码或者扫码枪扫码"
                        clearable
                        @keyup.enter="handleBarcodeEnter"
                      />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="商品代码:">
                      <span class="readonly-text">{{ itemForm.goodsCode || '-' }}</span>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="商品名称:">
                      <span class="readonly-text">{{ itemForm.goodsName || '-' }}</span>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="规格型号:">
                      <span class="readonly-text">{{ itemForm.spec || '-' }}</span>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="账面数量:">
                      <span class="readonly-text">{{ itemForm.bookNums ?? '-' }}</span>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="实盘数量:" prop="actualNums" required>
                      <el-input v-model="itemForm.actualNums" placeholder="请输入实盘数量">
                        <template #append>件</template>
                      </el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="盈亏:">
                      <span
                        class="readonly-text"
                        :class="{
                          'text-profit': itemDiff > 0,
                          'text-loss': itemDiff < 0
                        }"
                      >
                        {{ itemDiff }}
                      </span>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="备注:">
                      <el-input v-model="itemForm.remark" placeholder="备注" clearable />
                    </el-form-item>
                  </el-col>
                </el-row>
                <div class="item-entry__actions">
                  <el-button type="primary" @click="confirmAddItem">
                    <Icon icon="ep:check" class="mr-5px" />
                    {{ editingDetailIndex > -1 ? '保存修改' : '确认' }}
                  </el-button>
                  <el-button type="danger" @click="resetItemForm">
                    <Icon icon="ep:refresh" class="mr-5px" />重置
                  </el-button>
                </div>
              </el-form>

              <el-input
                v-model="productQuery.keyword"
                class="product-search"
                clearable
                placeholder="请输入商品名称、代码搜索"
                @keyup.enter="handleProductQuery"
                @clear="handleProductQuery"
              >
                <template #prefix>
                  <Icon icon="ep:search" />
                </template>
              </el-input>

              <el-table
                v-loading="productLoading"
                :data="productList"
                :stripe="true"
                row-key="id"
                height="320"
                :show-overflow-tooltip="true"
                :expand-row-keys="expandRowKeys"
                @expand-change="handleExpandChange"
              >
                <el-table-column type="expand">
                  <template #default="{ row }">
                    <div class="sku-panel">
                      <div class="sku-panel__tip">请选择下方规格后盘库</div>
                      <el-table
                        :data="getAttrValueList(row)"
                        size="small"
                        border
                        :show-overflow-tooltip="true"
                        empty-text="暂无规格明细"
                        @row-click="(sku) => handleSkuSelect(row, sku)"
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
                        <el-table-column label="库存" align="center" prop="stock" width="80" />
                        <el-table-column label="操作" align="center" width="80" fixed="right">
                          <template #default="{ row: sku }">
                            <el-button
                              link
                              type="primary"
                              @click.stop="handleSkuSelect(row, sku)"
                            >
                              选择
                            </el-button>
                          </template>
                        </el-table-column>
                      </el-table>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column label="商品代码" align="center" prop="barCode" min-width="100">
                  <template #default="{ row }">
                    {{ row.barCode || row.id }}
                  </template>
                </el-table-column>
                <el-table-column label="商品名称" align="center" prop="storeName" min-width="120" />
                <el-table-column label="规格" align="center" min-width="90">
                  <template #default="{ row }">
                    <el-tag size="small" :type="getAttrValueList(row).length > 1 ? 'warning' : 'info'">
                      {{ getAttrValueList(row).length || 0 }} 个规格
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="单位" align="center" prop="unitName" width="70" />
                <el-table-column label="库存" align="center" prop="stock" width="70" />
              </el-table>
              <Pagination
                :total="productTotal"
                v-model:page="productQuery.pageNo"
                v-model:limit="productQuery.pageSize"
                @pagination="getProductList"
              />
            </div>
          </div>
        </el-col>

        <!-- 右侧：基本信息 + 盘库详情 -->
        <el-col :span="12" class="stockcheck-form__col">
          <div class="panel panel--mb">
            <div class="panel__title">盘库基本信息</div>
            <div class="panel__body">
              <el-form
                ref="formRef"
                :model="formData"
                :rules="formRules"
                label-width="120px"
                size="default"
              >
                <el-form-item label="盘库日期:" prop="checkTime">
                  <el-date-picker
                    v-model="formData.checkTime"
                    type="date"
                    value-format="x"
                    format="YYYY 年 MM 月 DD 日"
                    placeholder="请选择盘库日期"
                    class="!w-full"
                  />
                </el-form-item>
                <el-form-item label="备注:" prop="remark">
                  <el-input v-model="formData.remark" placeholder="请输入备注内容" clearable />
                </el-form-item>
                <el-form-item label="盘盈合计:">
                  <span class="text-profit summary-num">{{ profitTotal }}</span>
                </el-form-item>
                <el-form-item label="盘亏合计:">
                  <span class="text-loss summary-num">{{ lossTotal }}</span>
                </el-form-item>
              </el-form>
            </div>
          </div>

          <div class="panel panel--flex">
            <div class="panel__title">盘库商品详情</div>
            <div class="panel__body panel__body--table">
              <el-table
                :data="detailList"
                :stripe="true"
                height="100%"
                :show-overflow-tooltip="true"
              >
                <el-table-column label="商品代码" align="center" prop="goodsCode" min-width="90" />
                <el-table-column label="商品名称" align="center" prop="goodsName" min-width="110" />
                <el-table-column label="规格" align="center" prop="spec" min-width="120" />
                <el-table-column label="账面" align="center" prop="bookNums" width="70" />
                <el-table-column label="实盘" align="center" prop="actualNums" width="70" />
                <el-table-column label="盈亏" align="center" prop="diffNums" width="70">
                  <template #default="{ row }">
                    <span
                      :class="{
                        'text-profit': row.diffNums > 0,
                        'text-loss': row.diffNums < 0
                      }"
                    >
                      {{ row.diffNums }}
                    </span>
                  </template>
                </el-table-column>
                <el-table-column label="单位" align="center" prop="unitName" width="60" />
                <el-table-column label="备注" align="center" prop="checkGoodsRemark" />
                <el-table-column label="操作" align="center" width="110" fixed="right">
                  <template #default="{ $index }">
                    <el-button link type="primary" @click="editDetail($index)">编辑</el-button>
                    <el-button link type="danger" @click="removeDetail($index)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import { StockCheckApi, StockCheckVO } from '@/api/stock/stockcheck'
import * as StoreProductApi from '@/api/mall/product/product'
import type {
  StoreProductVO,
  StoreProductAttrValueVO
} from '@/api/mall/product/product'

/** 商品盘库 表单 */
defineOptions({ name: 'StockCheckForm' })

interface DetailItem {
  stockGoodsId?: number
  stockGoodsValueId?: number
  goodsCode: string
  goodsName: string
  spec: string
  unitName: string
  bookNums: number
  actualNums: number
  diffNums: number
  checkGoodsRemark?: string
  barCode?: string
}

const { t } = useI18n()
const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref('')

const formData = ref({
  id: undefined as number | undefined,
  docnum: undefined as string | undefined,
  checkTime: undefined as number | string | undefined,
  checkTotalNums: undefined as number | undefined,
  profitNums: undefined as number | undefined,
  lossNums: undefined as number | undefined,
  remark: undefined as string | undefined,
  isAudit: undefined as number | undefined,
  auditTime: undefined as number | undefined
})

const formRules = reactive({
  checkTime: [{ required: true, message: '请选择盘库日期', trigger: 'change' }]
})
const formRef = ref()

const itemFormRef = ref()
const itemForm = ref({
  stockGoodsId: undefined as number | undefined,
  stockGoodsValueId: undefined as number | undefined,
  barCode: '',
  goodsCode: '',
  goodsName: '',
  spec: '',
  unitName: '',
  bookNums: 0 as number,
  actualNums: 0 as string | number,
  remark: ''
})
const itemFormRules = reactive({
  actualNums: [{ required: true, message: '请输入实盘数量', trigger: 'blur' }]
})

const detailList = ref<DetailItem[]>([])
const editingDetailIndex = ref(-1)

const itemDiff = computed(() => {
  const book = Number(itemForm.value.bookNums || 0)
  const actual = Number(itemForm.value.actualNums || 0)
  if (Number.isNaN(actual)) return 0
  return actual - book
})

const profitTotal = computed(() =>
  detailList.value.reduce((sum, item) => sum + (item.diffNums > 0 ? item.diffNums : 0), 0)
)
const lossTotal = computed(() =>
  detailList.value.reduce((sum, item) => sum + (item.diffNums < 0 ? Math.abs(item.diffNums) : 0), 0)
)

const productLoading = ref(false)
const productList = ref<StoreProductVO[]>([])
const productTotal = ref(0)
const expandRowKeys = ref<(string | number)[]>([])
const productQuery = reactive({
  pageNo: 1,
  pageSize: 10,
  keyword: '',
  isShow: 1
})

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

const getProductList = async () => {
  productLoading.value = true
  try {
    const params = {
      pageNo: productQuery.pageNo,
      pageSize: productQuery.pageSize,
      storeName: productQuery.keyword || undefined,
      isShow: productQuery.isShow
    }
    const data = await StoreProductApi.getStoreProductPage(params as any)
    productList.value = data.list || []
    productTotal.value = data.total || 0
    expandRowKeys.value = []
  } finally {
    productLoading.value = false
  }
}

const handleProductQuery = () => {
  productQuery.pageNo = 1
  getProductList()
}

const handleExpandChange = (_row: StoreProductVO, expandedRows: StoreProductVO[]) => {
  expandRowKeys.value = expandedRows.map((item) => item.id)
}

const handleSkuSelect = (product: StoreProductVO, sku: StoreProductAttrValueVO) => {
  if (!sku?.id) {
    message.warning('规格数据异常，请重新选择')
    return
  }
  const bookNums = Number(sku.stock ?? product.stock ?? 0)
  itemForm.value = {
    stockGoodsId: product.id,
    stockGoodsValueId: sku.id,
    barCode: getSkuBarCode(sku) || product.barCode || '',
    goodsCode: getSkuCode(sku) || product.barCode || String(product.id),
    goodsName: product.storeName || '',
    spec: getSkuName(sku),
    unitName: product.unitName || '件',
    bookNums,
    actualNums: bookNums,
    remark: ''
  }
  message.success(`已选择规格：${getSkuName(sku)}`)
}

const handleBarcodeEnter = async () => {
  const barCode = itemForm.value.barCode?.trim()
  if (!barCode) return
  productLoading.value = true
  try {
    const data = await StoreProductApi.getStoreProductPage({
      pageNo: 1,
      pageSize: 20,
      barCode,
      isShow: 1
    } as any)
    const list: StoreProductVO[] = data.list || []
    let matchedProduct: StoreProductVO | undefined
    let matchedSku: StoreProductAttrValueVO | undefined

    for (const product of list) {
      const skuList = getAttrValueList(product)
      const sku = skuList.find(
        (item) => getSkuBarCode(item) === barCode || getSkuCode(item) === barCode
      )
      if (sku) {
        matchedProduct = product
        matchedSku = sku
        break
      }
    }

    if (!matchedProduct) {
      matchedProduct =
        list.find((item) => item.barCode === barCode) ||
        list.find((item) => String(item.id) === barCode)
      if (matchedProduct) {
        const skuList = getAttrValueList(matchedProduct)
        matchedSku = skuList.length === 1 ? skuList[0] : undefined
      }
    }

    if (matchedProduct && matchedSku) {
      expandRowKeys.value = [matchedProduct.id]
      handleSkuSelect(matchedProduct, matchedSku)
    } else if (matchedProduct) {
      expandRowKeys.value = [matchedProduct.id]
      message.warning('请展开商品并选择具体规格后再盘库')
    } else {
      message.warning('未找到对应商品规格')
    }
  } finally {
    productLoading.value = false
  }
}

const resetItemForm = () => {
  itemForm.value = {
    stockGoodsId: undefined,
    stockGoodsValueId: undefined,
    barCode: '',
    goodsCode: '',
    goodsName: '',
    spec: '',
    unitName: '',
    bookNums: 0,
    actualNums: 0,
    remark: ''
  }
  editingDetailIndex.value = -1
  itemFormRef.value?.clearValidate()
}

const editDetail = (index: number) => {
  const item = detailList.value[index]
  if (!item) return
  editingDetailIndex.value = index
  itemForm.value = {
    stockGoodsId: item.stockGoodsId,
    stockGoodsValueId: item.stockGoodsValueId,
    barCode: item.barCode || '',
    goodsCode: item.goodsCode || '',
    goodsName: item.goodsName || '',
    spec: item.spec || '',
    unitName: item.unitName || '件',
    bookNums: item.bookNums,
    actualNums: item.actualNums,
    remark: item.checkGoodsRemark || ''
  }
  if (item.stockGoodsId) {
    expandRowKeys.value = [item.stockGoodsId]
  }
  message.success('已回显到左侧，修改后请点击确认')
}

const confirmAddItem = async () => {
  await itemFormRef.value?.validate()
  if (!itemForm.value.stockGoodsId || !itemForm.value.stockGoodsValueId) {
    message.warning('请先展开商品并选择一个规格后再盘库')
    return
  }
  const actualNums = Number(itemForm.value.actualNums)
  if (Number.isNaN(actualNums) || actualNums < 0) {
    message.warning('实盘数量不能小于0')
    return
  }
  const bookNums = Number(itemForm.value.bookNums || 0)
  const diffNums = actualNums - bookNums
  const detail: DetailItem = {
    stockGoodsId: itemForm.value.stockGoodsId,
    stockGoodsValueId: itemForm.value.stockGoodsValueId,
    goodsCode: itemForm.value.goodsCode,
    goodsName: itemForm.value.goodsName,
    spec: itemForm.value.spec,
    unitName: itemForm.value.unitName || '件',
    bookNums,
    actualNums,
    diffNums,
    checkGoodsRemark: itemForm.value.remark,
    barCode: itemForm.value.barCode
  }

  if (editingDetailIndex.value > -1) {
    const editIndex = editingDetailIndex.value
    const duplicateIndex = detailList.value.findIndex(
      (item, idx) =>
        idx !== editIndex &&
        item.stockGoodsId === detail.stockGoodsId &&
        item.stockGoodsValueId === detail.stockGoodsValueId
    )
    detailList.value[editIndex] = detail
    if (duplicateIndex > -1) {
      detailList.value.splice(duplicateIndex, 1)
    }
    message.success('已更新盘库商品')
    resetItemForm()
    return
  }

  const existIndex = detailList.value.findIndex(
    (item) =>
      item.stockGoodsId === itemForm.value.stockGoodsId &&
      item.stockGoodsValueId === itemForm.value.stockGoodsValueId
  )
  if (existIndex > -1) {
    detailList.value[existIndex] = detail
    message.success('已更新该规格盘库信息')
  } else {
    detailList.value.push(detail)
  }
  resetItemForm()
}

const removeDetail = (index: number) => {
  detailList.value.splice(index, 1)
  if (editingDetailIndex.value === index) {
    resetItemForm()
  } else if (editingDetailIndex.value > index) {
    editingDetailIndex.value -= 1
  }
}

const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  await getProductList()
  if (id) {
    formLoading.value = true
    try {
      const data = await StockCheckApi.getStockCheck(id)
      formData.value = {
        ...formData.value,
        ...data
      }
      const lists = (data as any).stockStockcheckListList || (data as any).items || []
      detailList.value = (lists || []).map((item: any) => ({
        stockGoodsId: item.stockGoodsId,
        stockGoodsValueId: item.stockGoodsValueId,
        goodsCode: item.goodsCode || String(item.stockGoodsId || ''),
        goodsName: item.stockGoodsName || item.goodsName || '',
        spec: item.stockGoodsValueName || item.spec || '',
        unitName: item.unitName || '件',
        bookNums: item.bookNums,
        actualNums: item.actualNums,
        diffNums: item.diffNums,
        checkGoodsRemark: item.checkGoodsRemark,
        barCode: item.barCode
      }))
    } finally {
      formLoading.value = false
    }
  }
}
defineExpose({ open })

const emit = defineEmits(['success'])

const submitForm = async () => {
  await formRef.value.validate()
  if (!detailList.value.length) {
    message.warning('请至少添加一件盘库商品')
    return
  }
  const invalid = detailList.value.some((item) => !item.stockGoodsValueId)
  if (invalid) {
    message.warning('盘库明细存在未选择规格的商品，请检查')
    return
  }
  formLoading.value = true
  try {
    const totalNums = detailList.value.reduce((sum, item) => sum + Number(item.actualNums || 0), 0)
    const data = {
      ...formData.value,
      checkTotalNums: totalNums,
      profitNums: profitTotal.value,
      lossNums: lossTotal.value,
      checkLists: detailList.value.map((item) => ({
        stockGoodsId: item.stockGoodsId,
        stockGoodsValueId: item.stockGoodsValueId,
        bookNums: item.bookNums,
        actualNums: item.actualNums,
        diffNums: item.diffNums,
        checkGoodsRemark: item.checkGoodsRemark
      }))
    } as unknown as StockCheckVO
    if (formType.value === 'create') {
      await StockCheckApi.createStockCheck(data)
      message.success(t('common.createSuccess'))
    } else {
      await StockCheckApi.updateStockCheck(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    emit('success')
  } finally {
    formLoading.value = false
  }
}

const resetForm = () => {
  formData.value = {
    id: undefined,
    docnum: undefined,
    checkTime: Date.now(),
    checkTotalNums: undefined,
    profitNums: undefined,
    lossNums: undefined,
    remark: undefined,
    isAudit: undefined,
    auditTime: undefined
  }
  detailList.value = []
  expandRowKeys.value = []
  resetItemForm()
  productQuery.pageNo = 1
  productQuery.keyword = ''
  formRef.value?.resetFields()
}
</script>

<style scoped lang="scss">
.stockcheck-form {
  min-height: 620px;

  &__row {
    height: 100%;
  }

  &__col {
    display: flex;
    flex-direction: column;
  }
}

.panel {
  border: 1px solid var(--el-border-color-lighter);
  border-radius: 4px;
  background: var(--el-bg-color);
  overflow: hidden;

  &--mb {
    margin-bottom: 12px;
  }

  &--flex {
    flex: 1;
    display: flex;
    flex-direction: column;
    min-height: 360px;
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
    padding: 12px;

    &--table {
      flex: 1;
      min-height: 0;
      padding-bottom: 0;
    }
  }
}

.item-entry {
  padding: 12px;
  margin-bottom: 12px;
  background: #f5f7fa;
  border-radius: 4px;

  :deep(.el-form-item) {
    margin-bottom: 12px;
  }

  &__actions {
    display: flex;
    justify-content: flex-end;
    gap: 8px;
  }
}

.readonly-text {
  color: var(--el-text-color-regular);
  line-height: 32px;
}

.product-search {
  margin-bottom: 10px;
}

.sku-panel {
  padding: 8px 12px 12px 48px;
  background: #fafafa;

  &__tip {
    margin-bottom: 8px;
    font-size: 12px;
    color: var(--el-color-warning);
  }
}

.text-profit {
  color: #67c23a;
  font-weight: 600;
}

.text-loss {
  color: #f56c6c;
  font-weight: 600;
}

.summary-num {
  font-size: 20px;
  line-height: 32px;
}
</style>
