<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
      :label-position="labelPosition"
    >
      <el-form-item label="标签组名称" prop="groupName">
        <el-input v-model="formData.groupName" placeholder="请输入标签组名称" class="!w-md" />
      </el-form-item>
      
      <el-form-item label="标签名称" >
        <VueDraggable handle=".cursor-move" :animation="400" item-key="index" v-model="tagsList">
          <template #item="{ element, index }">
            <div class="flex items-center mb-2" >
              <div class="w-md"><el-input v-model="element.name" placeholder="请输入标签名称" class="w-200" /></div>
              <Icon icon="ep:operation" class="ml-2 cursor-move" />
              <Icon icon="ep:delete" class="ml-2 cursor-pointer" @click="delTag(index)"/>
            </div>
          </template>
        </VueDraggable>
      </el-form-item>
    
      <div>
        <el-button
          type="primary"
          plain
          @click="addTag"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 添加标签
        </el-button>
      </div>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { CustomerTagGroupApi, CustomerTagGroupVO } from '@/api/crm/crmcustomertaggroup'
import VueDraggable from 'vuedraggable'
/** 客户标签分组 表单 */
defineOptions({ name: 'CustomerTagGroupForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  groupName: undefined,
  sort: undefined
})
const formRules = reactive({
  groupName: [{ required: true, message: '名称不能为空', trigger: 'blur' }],
})
const formRef = ref() // 表单 Ref
const labelPosition = ref<FormProps['labelPosition']>('top')
const tagsList = ref([{name:'',sort:0}])
const groupId = ref(0)

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    groupId.value = id
    try {
      formData.value = await CustomerTagGroupApi.getCustomerTagGroup(id)
      tagsList.value = formData.value.tagsList
    } finally {
      formLoading.value = false
    }
  }else{
    tagsList.value = [{name:'',sort:0}]
  }
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  //console.log('tagsList:',tagsList.value)
  tagsList.value.map((item,index) => {
    item.sort = index
    if(item.name == ''){
      message.warning('标签不能为空')
      return
    }
    if(groupId.value > 0){
      if(!item.groupId){
        item.groupId = groupId.value
      }
    }
  })
  //console.log('tagsList2:',tagsList.value)
  //return
  // 校验表单
  await formRef.value.validate()
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as CustomerTagGroupVO
    data.tagsList = tagsList.value
    if (formType.value === 'create') {
      await CustomerTagGroupApi.createCustomerTagGroup(data)
      message.success(t('common.createSuccess'))
    } else {
      await CustomerTagGroupApi.updateCustomerTagGroup(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}

const addTag = () => {
  tagsList.value.push({name:'',sort:0})
}
const delTag = (index) => {
  if(tagsList.value.length == 1){
    message.warning('请保留一个哦')
    return
  }
  tagsList.value.splice(index,1)
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: undefined,
    groupName: undefined,
    sort: undefined
  }
  formRef.value?.resetFields()
}
</script>