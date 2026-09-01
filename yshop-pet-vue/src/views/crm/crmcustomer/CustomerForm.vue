<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="70%">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <template v-for="group in enabledGroups" :key="group.id">
        <div v-if="enabledGroups.length > 1" class="customer-form-group-title">{{ group.name }}</div>
        <el-row>
          <el-col
            v-for="field in group.fields"
            :key="field.id"
            :span="field.fieldType === 'textarea' ? 24 : 12"
          >
            <DynamicCustomerField
              :field="field"
              :form-data="formData"
              :tags-arr="tagsArr"
              @update:form-data="onFormDataUpdate"
              @open-tags="handleOpenTags"
              @remove-tag="handleCloseTag"
              @open-map="selectAddr"
            />
          </el-col>
        </el-row>
      </template>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
  <Tmap ref="mapRef" @mapConfirm="getMapAddr" />
  <tag ref="tagRef" @success="getTags" />
</template>

<script setup lang="ts">
import { CustomerApi, CustomerVO } from '@/api/crm/crmcustomer'
import Tmap from '@/views/crm/crmcustomer/components/map/map.vue'
import tag from '@/views/components/customer/tag.vue'
import DynamicCustomerField from './components/DynamicCustomerField.vue'
import type { FormGroup } from '@/views/crm/crmform/types'
import {
  loadCustomerFormGroups,
  getEnabledGroups,
  getEnabledFields,
  buildFormRules,
  buildCustomerSystemDefaults,
  stripCustomFields,
  isCustomFieldKey
} from '@/views/crm/crmform/utils/formConfig'
import {
  getCustomerExt,
  saveCustomerExt,
  pickCustomFields
} from '@/views/crm/crmform/utils/customerExtData'

/** 客户 表单 */
defineOptions({ name: 'CustomerForm' })

const { t } = useI18n()
const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref('')
const formGroups = ref<FormGroup[]>([])
const formData = ref<Record<string, any>>({})
const formRef = ref()
const tagsArr = ref<string[]>([])
const tagRef = ref()
const mapRef = ref()

const enabledGroups = computed(() => getEnabledGroups(formGroups.value))
const enabledFields = computed(() => getEnabledFields(formGroups.value))
const formRules = computed(() => buildFormRules(enabledFields.value))

const onFormDataUpdate = (val: Record<string, any>) => {
  formData.value = val
}

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  formLoading.value = true
  try {
    formGroups.value = await loadCustomerFormGroups()
    resetForm()

    if (id) {
      const data = await CustomerApi.getCustomer(id)
      const ext = await getCustomerExt(id)
      formData.value = { ...formData.value, ...data, ...ext }
      tagsArr.value = data.tags ? String(data.tags).split(',').filter(Boolean) : []
    }
  } finally {
    formLoading.value = false
  }
}
defineExpose({ open })

const emit = defineEmits(['success'])

const submitForm = async () => {
  await formRef.value.validate()
  formLoading.value = true
  try {
    const customPayload = pickCustomFields(formData.value)
    const data = stripCustomFields({ ...formData.value }) as unknown as CustomerVO
    if (Array.isArray((data as any).tags)) {
      ;(data as any).tags = (data as any).tags.toString()
    }
    if (formType.value === 'create') {
      const createdId = await CustomerApi.createCustomer(data)
      const idNum = typeof createdId === 'number' ? createdId : Number(createdId)
      if (!Number.isNaN(idNum) && idNum > 0) {
        await saveCustomerExt(idNum, customPayload)
      } else if (Object.keys(customPayload).length > 0) {
        message.warning('客户已创建，但未拿到 id，自定义字段未能保存')
      }
      message.success(t('common.createSuccess'))
    } else {
      await CustomerApi.updateCustomer(data)
      if (data.id != null) {
        await saveCustomerExt(data.id, customPayload)
      }
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    emit('success')
  } finally {
    formLoading.value = false
  }
}

const handleOpenTags = () => {
  tagRef.value?.open(tagsArr.value)
}

const getTags = (arr: string[]) => {
  tagsArr.value = arr
  formData.value = {
    ...formData.value,
    tags: tagsArr.value.toString()
  }
}

const handleCloseTag = (index: number) => {
  tagsArr.value.splice(index, 1)
  formData.value = {
    ...formData.value,
    tags: tagsArr.value.toString()
  }
}

const selectAddr = () => {
  mapRef.value?.open()
}

const getMapAddr = (data: any) => {
  formData.value = {
    ...formData.value,
    addressStr: data.address,
    detailAddress: data.detailAddress,
    lng: data.longitude,
    lat: data.latitude
  }
}

const resetForm = () => {
  const defaults = buildCustomerSystemDefaults(enabledFields.value)
  for (const field of enabledFields.value) {
    if (isCustomFieldKey(field.fieldKey)) {
      defaults[field.fieldKey] =
        field.defaultValue ?? (field.fieldType === 'checkbox' ? [] : undefined)
    }
  }
  formData.value = defaults
  tagsArr.value = []
  formRef.value?.resetFields()
}
</script>

<style scoped>
.customer-form-group-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  margin: 8px 0 12px;
  padding-left: 4px;
  border-left: 3px solid var(--el-color-primary);
}
</style>
