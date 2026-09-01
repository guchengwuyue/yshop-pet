<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="80px"
    >
      <template v-for="field in searchFields" :key="field.id">
        <el-form-item :label="field.label" :prop="field.fieldKey">
          <el-select
            v-if="field.fieldType === 'select' || field.fieldType === 'radio'"
            v-model="queryParams[field.fieldKey]"
            clearable
            :placeholder="field.placeholder || `请选择${field.label}`"
            class="!w-240px"
          >
            <el-option
              v-for="dict in getSearchDictOptions(field)"
              :key="String(dict.value)"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
          <el-input
            v-else
            v-model="queryParams[field.fieldKey]"
            :placeholder="field.placeholder || `请输入${field.label}`"
            clearable
            @keyup.enter="handleQuery"
            class="!w-240px"
          />
        </el-form-item>
      </template>
      <el-form-item>
        <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
        <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 刷新</el-button>
        <el-button
          type="primary"
          plain
          @click="openForm('create')"
          v-hasPermi="['crm:customer:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button
          type="warning"
          plain
          @click="handleImport"
          v-hasPermi="['crm:customer:import']"
        >
          <Icon icon="ep:upload" class="mr-5px" /> 导入
        </el-button>
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
    >
      <el-table-column type="selection" width="40" fixed />
      <el-table-column
        v-for="field in tableFields"
        :key="field.id"
        :label="field.label"
        :prop="field.fieldKey"
        :align="'center'"
        :width="columnWidth(field)"
        :fixed="field.fieldKey === 'name' ? true : undefined"
      >
        <template #default="scope">
          <template v-if="field.fieldKey === 'name'">
            <div
              @click="openDetail(scope.row.id, scope.row)"
              style="color: #409eff; cursor: pointer"
            >
              {{ scope.row.name }}
            </div>
            <div
              v-if="rowTags(scope.row).length > 0"
              style="
                display: flex;
                flex-wrap: wrap;
                text-align: center;
                justify-content: center;
              "
            >
              <el-tag
                type="danger"
                class="mr-1"
                size="small"
                v-for="(tag, idx) in rowTags(scope.row)"
                :key="idx"
              >
                {{ tag }}
              </el-tag>
            </div>
          </template>
          <dict-tag
            v-else-if="field.dictType && field.dictType !== 'area_cascader'"
            :type="field.dictType"
            :value="scope.row[field.fieldKey]"
          />
          <span v-else-if="field.fieldType === 'datetime'">
            {{ formatFieldDisplay(field, scope.row[field.fieldKey]) }}
          </span>
          <span v-else>{{ formatFieldDisplay(field, scope.row[field.fieldKey]) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column label="操作" align="center" >
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['crm:customer:update']"
            v-if="scope.row.permission == 'all' || scope.row.permission == 'edit'"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['crm:customer:delete']"
            v-if="scope.row.permission == 'all'"
          >
            删除
          </el-button>
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
  <CustomerForm ref="formRef" @success="getList" />
  <CustomerFormImport ref="formImportRef" @success="getList" />
  <CustomerDetail ref="customerDetailRef" @success="getList" />
</template>

<script setup lang="ts">
import { dateFormatter, beginOfDay, endOfDay, formatDate } from '@/utils/formatTime'
import { CustomerApi } from '@/api/crm/crmcustomer'
import CustomerForm from './CustomerForm.vue'
import CustomerFormImport from './CustomerFormImport.vue'
import { getIntDictOptions } from '@/utils/dict'
import RecordForm from '@/views/crm/crmrecord/RecordForm.vue'
import CustomerDetail from '@/views/crm/crmcustomer/CustomerDetail.vue'
import type { FormField, FormGroup } from '@/views/crm/crmform/types'
import {
  loadCustomerFormGroups,
  getEnabledFields,
  CUSTOMER_LIST_SEARCHABLE_KEYS,
  CUSTOMER_LIST_TABLE_KEYS,
  formatFieldDisplay,
  AREA_CASCADER_DICT
} from '@/views/crm/crmform/utils/formConfig'
import {
  mergeCustomerExtList,
  removeCustomerExt
} from '@/views/crm/crmform/utils/customerExtData'

/** 客户 列表 */
defineOptions({ name: 'CrmCustomer' })

const message = useMessage()
const { t } = useI18n()

const loading = ref(true)
const list = ref<Record<string, any>[]>([])
const total = ref(0)
const queryParams = reactive<Record<string, any>>({
  pageNo: 1,
  pageSize: 10,
  name: undefined,
  mobile: undefined,
  telephone: undefined,
  dealStatus: undefined,
  level: undefined,
  industry: undefined,
  tags: undefined,
  source: undefined,
  remark: undefined,
  userId: undefined,
  weixin: undefined,
  qq: undefined,
  followStatus: undefined,
  relation: 'all'
})
const queryFormRef = ref()
const activeIndex = ref('all')
const selectCustomers = ref([])
const isDisabled = ref(true)
const formGroups = ref<FormGroup[]>([])

const enabledFields = computed(() => getEnabledFields(formGroups.value))

const searchFields = computed(() =>
  enabledFields.value.filter((f) => CUSTOMER_LIST_SEARCHABLE_KEYS.has(f.fieldKey))
)

/** 表格动态列：跳过 tags（挂在 name 下展示） */
const tableFields = computed(() =>
  enabledFields.value.filter(
    (f) => CUSTOMER_LIST_TABLE_KEYS.has(f.fieldKey) && f.fieldKey !== 'tags'
  )
)

const hasFieldKey = (key: string) => enabledFields.value.some((f) => f.fieldKey === key)

const columnWidth = (field: FormField) => {
  if (field.fieldKey === 'name') return 260
  if (field.fieldType === 'datetime') return 180
  if (field.fieldKey === 'mobile' || field.fieldKey === 'telephone') return 120
  if (field.fieldKey === 'weixin' || field.fieldKey === 'qq') return 120
  if (field.fieldKey === 'level' || field.fieldKey === 'industry' || field.fieldKey === 'source')
    return 90
  return undefined
}

const getSearchDictOptions = (field: FormField) => {
  if (!field.dictType || field.dictType === AREA_CASCADER_DICT) return []
  return getIntDictOptions(field.dictType)
}

const rowTags = (row: Record<string, any>) => {
  const tags = row.tags
  if (!tags) return []
  if (Array.isArray(tags)) return tags.filter(Boolean)
  return String(tags).split(',').filter(Boolean)
}

/** 查询列表 */
const getList = async () => {
  isDisabled.value = true
  loading.value = true
  try {
    formGroups.value = await loadCustomerFormGroups()
    const data = await CustomerApi.getCustomerPage2(queryParams)
    const merged = await mergeCustomerExtList(data.list || [])
    list.value = merged.map((row) => {
      if (row.tags && typeof row.tags === 'string' && row.tags !== '') {
        return { ...row, tags: row.tags.split(',') }
      }
      return row
    })
    total.value = data.total
  } finally {
    loading.value = false
  }
}

const handleSelectionChange = (val) => {
  if (val.length > 0) {
    val.forEach((e) => {
      if (e.permission != 'all') {
        isDisabled.value = true
        message.error('有不可以操作数据权限')
        return
      } else {
        isDisabled.value = false
      }
    })
  }
  selectCustomers.value = val
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

const handleSelect = (key) => {
  queryParams.nextTime = undefined
  if (key == 'today') {
    queryParams.nextTime = [
      formatDate(beginOfDay(new Date())),
      formatDate(endOfDay(new Date()))
    ]
    key = 'my'
  }
  queryParams.relation = key
  getList()
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

const recordFormRef = ref()
const openRecord = (id?: number) => {
  recordFormRef.value.open('', id)
}

const customerDetailRef = ref()
const openDetail = (id?: number, data?) => {
  customerDetailRef.value.open('', id, data)
}

const handleOpen = async (id: number) => {
  try {
    await message.confirm('确定把客户放入公海')
    await CustomerApi.openCustomer(id)
    message.success('放入成功')
    await getList()
  } catch {}
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    await message.delConfirm()
    await CustomerApi.deleteCustomer(id)
    removeCustomerExt(id)
    message.success(t('common.delSuccess'))
    await getList()
  } catch {}
}

const formImportRef = ref()
const handleImport = () => {
  formImportRef.value.open()
}

const formRecoverRef = ref()
const openRecover = () => {
  formRecoverRef.value.open()
}

const formTransferRef = ref()
const openTransfer = () => {
  formTransferRef.value.open(selectCustomers.value)
}

const smsTemplateSendFormRef = ref()
const openSms = () => {
  smsTemplateSendFormRef.value.open(selectCustomers.value, true)
}

const mailTemplateSendFormRef = ref()
const openMail = () => {
  mailTemplateSendFormRef.value.open(selectCustomers.value, true)
}

/** 初始化 **/
onMounted(async () => {
  formGroups.value = await loadCustomerFormGroups()
  await getList()
})
</script>
