<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-form-item label="上传" prop="name">
        <el-upload
          ref="uploadRef"
          v-model:file-list="fileList"
          :action="importUrl + '?deptId=' + deptId + '&adminIds='+adminIds + '&averageType=' + averageType"
          :limit="1"
          :headers="uploadHeaders"
          :on-error="submitFormError"
          :on-exceed="handleExceed"
          :on-success="submitFormSuccess"
          :auto-upload="false"
          accept=".xlsx, .xls"
        >
          <template #trigger>
            <el-button type="primary">上传导入文档</el-button>
          </template>
          <!-- <el-button class="ml-3" type="success" @click="submitUpload">
            upload to server
          </el-button> -->
          <template #tip>
            <div style="color:red;">
              请按照模板填好上传
            </div>
            <span>仅允许导入 xls、xlsx 格式文件。</span>
            <el-link
            :underline="false"
            style="font-size: 12px; vertical-align: baseline"
            type="primary"
            @click="importTemplate"
          >
            下载模板
          </el-link>
          </template>
        </el-upload>
      </el-form-item>
      <el-form-item label="归属部门" prop="mobile">
        <el-tree-select
            v-model="deptId"
            :data="deptList"
            :props="defaultProps"
            check-strictly
            node-key="id"
            placeholder="请选择归属部门"
          />
      </el-form-item>
      <el-form-item label="归属员工" prop="telephone">
        <el-select v-model="adminIds" clearable multiple placeholder="选择用户" >
          <el-option
            v-for="item in userList"
            :key="item.id"
            :label="item.nickname"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="分配方式" prop="level">
        <el-radio-group v-model="averageType">
          <el-radio :value="1">平均分配</el-radio>
          <el-radio :value="2">随机分配</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { CustomerApi, CustomerVO } from '@/api/crm/crmcustomer'
import download from '@/utils/download'
import * as UserApi from '@/api/system/user'
import * as DeptApi from '@/api/system/dept'
import { defaultProps, handleTree } from '@/utils/tree'
import { getAccessToken, getTenantId } from '@/utils/auth'

/** 客户 表单 */
defineOptions({ name: 'CustomerForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const importUrl =
  import.meta.env.VITE_BASE_URL + import.meta.env.VITE_API_URL + '/crm/customer/import'
const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('批量导入') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const averageType = ref(1)
const uploadHeaders = ref() // 上传 Header 头
const fileList = ref([]) // 文件列表
const formRef = ref() // 表单 Ref
const uploadRef = ref()
const deptId = ref(undefined)
const adminIds = ref([])

const userList = ref<UserApi.UserVO[]>([]) // 用户列表
const deptList = ref<Tree[]>([]) // 树形结构
/** 打开弹窗 */
const open = async () => {
  dialogVisible.value = true
  fileList.value = []
  resetForm()
  userList.value = await UserApi.getAccessUserList()
  deptList.value = handleTree(await DeptApi.getAccessDeptList())
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

const submitForm = async () => {
  if (fileList.value.length == 0) {
    message.error('请上传文件')
    return
  }
  if(!deptId.value && adminIds.value.length == 0){
    message.error('请选择部门或者员工')
    return
  }
  // 提交请求
  uploadHeaders.value = {
    Authorization: 'Bearer ' + getAccessToken(),
    'tenant-id': getTenantId()
  }
 
  formLoading.value = true
  uploadRef.value!.submit()
  
}

/** 文件上传成功 */
const emits = defineEmits(['success'])
const submitFormSuccess = (response: any) => {
  if (response.code !== 0) {
    message.error(response.msg)
    formLoading.value = false
    resetForm()
    return
  }
  // 拼接提示语
  const data = response.data
  let text = '上传成功数量：' + data.createNames.length + ';'
  for (let username of data.createNames) {
    text += '< ' + username + ' >'
  }
  text += '更新失败数量：' + Object.keys(data.failureNames).length + ';'
  for (const username in data.failureNames) {
    text += '< ' + username + ': ' + data.failureNames[username] + ' >'
  }
  message.alert(text)
  formLoading.value = false
  dialogVisible.value = false
  // 发送操作成功的事件
  emits('success')
}

/** 上传错误提示 */
const submitFormError = (): void => {
  message.error('上传失败，请您重新上传！')
  formLoading.value = false
}

const handleExceed = (): void => {
  message.error('最多只能上传一个文件！')
}

const resetForm = async (): Promise<void> => {
  // 重置上传状态和文件
  formLoading.value = false
  await nextTick()
  uploadRef.value?.clearFiles()
}


const importTemplate = async () => {
  const res = await CustomerApi.importTemplate()
  download.excel(res, '客户导入模版.xls')
}



</script>
<style>
/* .el-card__header {
  background-color:#66b1ff
} */
</style>