<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="75%">
    <el-row v-loading="loading">
      <el-col :span="24">
        <el-card>
          <template #header>
            <el-row>
              <el-col :span="12">
                <div style="display: flex; align-items: center">
                  <el-image style="width: 40px; height: 30px" :src="customerinfoimg" />
                  <span style="margin-left: 8px; font-weight: bolder">{{ formData2.name }}</span>
                </div>
              </el-col>
            </el-row>
          </template>

          <div>
            <template v-for="group in enabledGroups" :key="group.id">
              <el-descriptions :title="group.name" class="mb-12px">
                <el-descriptions-item
                  v-for="field in group.fields"
                  :key="field.id"
                  :label="field.label"
                >
                  <dict-tag
                    v-if="field.dictType && field.dictType !== 'area_cascader'"
                    :type="field.dictType"
                    :value="formData2[field.fieldKey]"
                  />
                  <template v-else-if="field.fieldKey === 'tags'">
                    <el-tag
                      v-for="(tag, idx) in tagList"
                      :key="idx"
                      type="danger"
                      class="mr-1"
                      size="small"
                    >
                      {{ tag }}
                    </el-tag>
                  </template>
                  <span v-else-if="field.fieldKey === 'addressStr'">
                    {{ formData2.addressStr || '未填写' }}
                  </span>
                  <span v-else>{{ formatFieldDisplay(field, formData2[field.fieldKey]) }}</span>
                </el-descriptions-item>
              </el-descriptions>
            </template>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row v-loading="loading">
      <el-col :span="24" style="margin-top: 10px">
        <el-tabs type="border-card">
          <el-tab-pane label="操作记录">
            <Log type="CRM_CUSTOMER" :bizId="customerId" />
          </el-tab-pane>
        </el-tabs>
      </el-col>
    </el-row>
    <template #footer>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import { CustomerApi } from '@/api/crm/crmcustomer'
import Log from '@/views/components/log/index.vue'
import customerinfoimg from '@/assets/imgs/customerinfo.png'
import { useUserStore } from '@/store/modules/user'
import type { FormGroup } from '@/views/crm/crmform/types'
import {
  loadCustomerFormGroups,
  getEnabledGroups,
  formatFieldDisplay
} from '@/views/crm/crmform/utils/formConfig'
import { getCustomerExt } from '@/views/crm/crmform/utils/customerExtData'

const userStore = useUserStore()

defineOptions({ name: 'CustomerDetail' })

const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formData2 = ref<Record<string, any>>({})
const myId = ref(0)
const customerId = ref(0)
const customerData = ref<Record<string, any>>({})
const loading = ref(false)
const from = ref('')
const isDetail = ref(true)
const formGroups = ref<FormGroup[]>([])

const enabledGroups = computed(() => getEnabledGroups(formGroups.value))
const tagList = computed(() => {
  const tags = formData2.value.tags
  if (!tags) return []
  if (Array.isArray(tags)) return tags.filter(Boolean)
  return String(tags).split(',').filter(Boolean)
})

/** 打开弹窗 */
const open = async (type: string, id?: number, data?: Object, fromStr?: string) => {
  if (fromStr == 'open') {
    isDetail.value = false
  }
  from.value = fromStr || ''
  customerData.value = (data || {}) as Record<string, any>
  customerId.value = id as number
  dialogVisible.value = true
  dialogTitle.value = '客户详情'
  loading.value = true
  try {
    const [groups, customer] = await Promise.all([
      loadCustomerFormGroups(),
      CustomerApi.getCustomer(id as number)
    ])
    formGroups.value = groups
    const ext = await getCustomerExt(id)
    formData2.value = { ...customer, ...ext }
  } finally {
    loading.value = false
  }
}
defineExpose({ open })

const getList = () => {
  dialogVisible.value = false
  emit('success')
}

const emit = defineEmits(['success'])

const call = async (id, phone) => {
  await CloudcallApi.call({ id: id, source: 'customer', phone: phone })
  message.success('云呼叫成功，请等待')
}

const formTransferRef = ref()
const openTransfer = () => {
  formTransferRef.value.open([formData2.value])
}

const formRecoverRef = ref()
const openRecover = () => {
  formRecoverRef.value.open()
}

const smsTemplateSendFormRef = ref()
const openSms = () => {
  smsTemplateSendFormRef.value.open([formData2.value], true)
}

const mailTemplateSendFormRef = ref()
const openMail = () => {
  mailTemplateSendFormRef.value.open([formData2.value], true)
}

const handleOpen = async (id: number) => {
  try {
    await message.confirm('确定把客户放入公海')
    await CustomerApi.openCustomer(id)
    message.success('放入成功')
    getList()
  } catch {}
}
</script>

<style lang="scss" scoped>
:deep(.el-icon svg) {
  height: 0;
  width: 0;
}

:deep(.el-input.is-disabled .el-input__wrapper) {
  box-shadow: none;
  background-color: #ffffff !important;
}

:deep(.el-cascader .el-input__inner) {
  border: none !important;
}

.mb-12px {
  margin-bottom: 12px;
}
</style>
