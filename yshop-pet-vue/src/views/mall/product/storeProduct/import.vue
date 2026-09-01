<template>
  <Dialog v-model="dialogVisible" title="导入" width="560px">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="90px"
      v-loading="formLoading"
    >
      <el-form-item label="商品类别:" prop="cateId">
        <el-tree-select
          v-model="formData.cateId"
          :data="categoryTree"
          :props="{ label: 'name', value: 'id' }"
          check-strictly
          default-expand-all
          placeholder="==请选择=="
          class="!w-full"
        />
      </el-form-item>
      <el-alert
        type="info"
        :closable="false"
        show-icon
        class="mb-18px import-tip"
        title="导入的商品将全部分配到当前所选商品类别下。"
      />
      <el-form-item label="导入文件:" required>
        <div class="import-file-row">
          <el-upload
            ref="uploadRef"
            v-model:file-list="fileList"
            :action="importUrl"
            :auto-upload="false"
            :disabled="formLoading"
            :headers="uploadHeaders"
            :limit="1"
            :on-change="handleFileChange"
            :on-error="submitFormError"
            :on-exceed="handleExceed"
            :on-success="submitFormSuccess"
            :show-file-list="false"
            accept=".xlsx, .xls"
            class="import-upload"
          >
            <el-input
              :model-value="fileName"
              placeholder="请选择导入文件"
              readonly
              class="import-file-input"
            />
          </el-upload>
          <el-button type="danger" :disabled="formLoading" @click="submitForm">
            <Icon icon="ep:upload" class="mr-5px" />
            上传
          </el-button>
          <el-button type="primary" :disabled="formLoading" @click="importTemplate">
            <Icon icon="ep:download" class="mr-5px" />
            下载模板
          </el-button>
        </div>
      </el-form-item>
    </el-form>
  </Dialog>
</template>

<script lang="ts" setup>
import * as StoreProductApi from '@/api/mall/product/product'
import * as ProductCategoryApi from '@/api/mall/product/category'
import { getAccessToken, getTenantId } from '@/utils/auth'
import download from '@/utils/download'
import { handleTree } from '@/utils/tree'

defineOptions({ name: 'StoreProductImport' })

const message = useMessage()
const emit = defineEmits(['success'])

const dialogVisible = ref(false)
const formLoading = ref(false)
const formRef = ref()
const uploadRef = ref()
const fileList = ref<any[]>([])
const fileName = ref('')
const uploadHeaders = ref<Record<string, string>>()
const categoryTree = ref<any[]>([])
const formData = reactive<{ cateId?: number }>({
  cateId: undefined
})
const formRules = reactive({
  cateId: [{ required: true, message: '请选择商品类别', trigger: 'change' }]
})

const importUrl = computed(() => {
  return (
    import.meta.env.VITE_BASE_URL +
    import.meta.env.VITE_API_URL +
    `/product/store-product/import?cateId=${formData.cateId}`
  )
})

/** 打开弹窗 */
const open = async () => {
  dialogVisible.value = true
  fileList.value = []
  fileName.value = ''
  formData.cateId = undefined
  await resetForm()
  await getCategoryTree()
  formRef.value?.clearValidate()
}
defineExpose({ open })

/** 加载商品分类 */
const getCategoryTree = async () => {
  const res = await ProductCategoryApi.getCategoryList('')
  categoryTree.value = handleTree(res)
}

/** 选择文件（点击输入框触发 el-upload 选文件） */
const handleFileChange = (file: any) => {
  fileName.value = file?.name || ''
}

/** 点击上传：商品归属当前所选类别，走后端批量创建 */
const submitForm = async () => {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  if (fileList.value.length === 0) {
    message.error('请选择导入文件')
    return
  }
  uploadHeaders.value = {
    Authorization: 'Bearer ' + getAccessToken(),
    'tenant-id': getTenantId()
  }
  formLoading.value = true
  uploadRef.value!.action = importUrl.value
  uploadRef.value!.submit()
}

/** 上传成功 */
const submitFormSuccess = (response: any) => {
  if (response.code !== 0) {
    message.error(response.msg || '导入失败')
    formLoading.value = false
    return
  }
  const data = response.data || {}
  const createNames = data.createNames || data.createStoreNames || []
  const failureNames = data.failureNames || data.failureStoreNames || {}
  let text = `导入成功数量：${createNames.length}；`
  for (const name of createNames) {
    text += `< ${name} >`
  }
  text += `导入失败数量：${Object.keys(failureNames).length}；`
  for (const name in failureNames) {
    text += `< ${name}: ${failureNames[name]} >`
  }
  message.alert(text)
  formLoading.value = false
  dialogVisible.value = false
  emit('success')
}

/** 上传失败 */
const submitFormError = () => {
  message.error('上传失败，请您重新上传！')
  formLoading.value = false
}

/** 超出数量限制 */
const handleExceed = () => {
  message.error('最多只能上传一个文件！')
}

/** 重置上传 */
const resetForm = async () => {
  formLoading.value = false
  await nextTick()
  uploadRef.value?.clearFiles()
}

/** 下载模板 */
const importTemplate = async () => {
  const res = await StoreProductApi.importStoreProductTemplate()
  download.excel(res, '商品导入模板.xls')
}
</script>

<style scoped>
.import-tip {
  line-height: 1.6;
}

.import-file-row {
  display: flex;
  align-items: center;
  gap: 8px;
  width: 100%;
}

.import-upload {
  flex: 1;
  min-width: 0;
}

.import-upload :deep(.el-upload) {
  width: 100%;
  display: block;
}

.import-file-input {
  width: 100%;
}
</style>
