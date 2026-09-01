<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="名称" prop="name">
        <el-input v-model="formData.name" placeholder="请输入名称" />
      </el-form-item>
      <el-form-item label="简称" prop="shortName">
        <el-input v-model="formData.shortName" placeholder="请输入简称" />
      </el-form-item>
      <el-form-item label="代码" prop="code">
        <el-input v-model="formData.code" placeholder="请输入代码" />
      </el-form-item>
      <el-form-item label="发货地址" prop="shipAddress">
        <el-input v-model="formData.shipAddress" placeholder="请输入发货地址" />
      </el-form-item>
      <el-form-item label="联系地址" prop="address">
        <el-input v-model="formData.address" placeholder="请输入联系地址" />
      </el-form-item>
      <el-form-item label="联系人" prop="contacts">
        <el-input v-model="formData.contacts" placeholder="请输入联系人" />
      </el-form-item>
      <el-form-item label="联系电话" prop="telphone">
        <el-input v-model="formData.telphone" placeholder="请输入联系电话" />
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input type="textarea" size="5" v-model="formData.remark" placeholder="请输入备注" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { SupplierApi, SupplierVO } from '@/api/stock/stocksupplier'

/** 供应商 表单 */
defineOptions({ name: 'SupplierForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  name: undefined,
  shortName: undefined,
  code: undefined,
  shipAddress: undefined,
  address: undefined,
  contacts: undefined,
  telphone: undefined,
  remark: undefined
})
const formRules = reactive({
  name: [{ required: true, message: '请填写名称', trigger: 'blur' }],
  telphone: [{ required: true, message: '请填写联系电话', trigger: 'blur' }],
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await SupplierApi.getSupplier(id)
    } finally {
      formLoading.value = false
    }
  }
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  await formRef.value.validate()
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as SupplierVO
    if (formType.value === 'create') {
      await SupplierApi.createSupplier(data)
      message.success(t('common.createSuccess'))
    } else {
      await SupplierApi.updateSupplier(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: undefined,
    name: undefined,
    shortName: undefined,
    code: undefined,
    shipAddress: undefined,
    address: undefined,
    contacts: undefined,
    telphone: undefined,
    remark: undefined
  }
  formRef.value?.resetFields()
}
</script>