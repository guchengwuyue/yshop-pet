<template>
  <ContentWrap>
    <div class="page-header">
      <div class="header-left">
        <span class="page-title">{{ pageTitle }}</span>
        <span class="page-desc">{{ pageDesc }}</span>
      </div>
      <div class="header-right">
        <el-button @click="handleReset">恢复默认</el-button>
        <el-button @click="handleAddGroup">
          <Icon icon="ep:plus" class="mr-5px" />
          添加分组
        </el-button>
        <el-button type="primary" :loading="saveLoading" @click="handleSave">
          <Icon icon="ep:check" class="mr-5px" />
          保存
        </el-button>
      </div>
    </div>
  </ContentWrap>

  <ContentWrap v-loading="loading">
    <FormGroupCard
      v-for="(group, index) in groups"
      :key="group.id"
      :group="group"
      @update:group="(g) => onUpdateGroup(index, g)"
      @add-field="handleAddField"
      @delete-field="handleDeleteField"
      @move-field="openMoveDialog"
      @delete-group="handleDeleteGroup"
      @preview="handlePreview"
    />

    <el-empty v-if="!loading && groups.length === 0" description="暂无分组，请添加分组" />
  </ContentWrap>

  <MoveGroupDialog
    ref="moveDialogRef"
    :groups="groups"
    :current-group-id="moveContext.groupId"
    @confirm="handleMoveConfirm"
  />

  <!-- 简单预览 -->
  <Dialog title="分组字段预览" v-model="previewVisible" width="520px">
    <template v-if="previewGroup">
      <el-descriptions
        v-if="previewGroup.fields.some((f) => f.enabled)"
        :column="1"
        border
      >
        <el-descriptions-item
          v-for="field in previewGroup.fields.filter((f) => f.enabled)"
          :key="field.id"
          :label="field.label"
          :label-class-name="field.required ? 'required-label' : ''"
        >
          {{ fieldTypeLabel(field.fieldType) }}
          <span v-if="field.required" class="preview-required">（必填）</span>
        </el-descriptions-item>
      </el-descriptions>
      <el-empty v-else description="该分组暂无启用字段" />
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import { ElMessageBox } from 'element-plus'
import { CrmFormApi } from '@/api/crm/crmform'
import type { FormField, FormGroup, FormModule } from './types'
import { FIELD_TYPE_OPTIONS } from './types'
import FormGroupCard from './components/FormGroupCard.vue'
import MoveGroupDialog from './components/MoveGroupDialog.vue'
const isDemo = import.meta.env.IS_DEMO

defineOptions({ name: 'CrmForm' })

const message = useMessage()
const { t } = useI18n()

const currentModule = ref<FormModule>('customer')
const loading = ref(false)
const saveLoading = ref(false)
const groups = ref<FormGroup[]>([])

const moveDialogRef = ref<InstanceType<typeof MoveGroupDialog>>()
const moveContext = reactive({
  groupId: '',
  fieldIndex: -1
})

const previewVisible = ref(false)
const previewGroup = ref<FormGroup | null>(null)

const pageTitle = computed(() =>
  currentModule.value === 'customer' ? '客户表单配置' : '线索表单配置'
)
const pageDesc = computed(() =>
  currentModule.value === 'customer'
    ? '自定义客户表单字段，支持系统字段启停与自定义字段'
    : '自定义线索表单字段，支持系统字段启停与自定义字段'
)

const fieldTypeLabel = (type: string) =>
  FIELD_TYPE_OPTIONS.find((o) => o.value === type)?.label || type

const genId = (prefix: string) => `${prefix}_${Date.now()}_${Math.random().toString(36).slice(2, 8)}`

/** 加载配置 */
const loadConfig = async () => {
  loading.value = true
  try {
    groups.value = await CrmFormApi.getFormConfig(currentModule.value)
  } finally {
    loading.value = false
  }
}

const onModuleChange = () => {
  loadConfig()
}

const onUpdateGroup = (index: number, group: FormGroup) => {
  groups.value[index] = group
}

/** 添加分组 */
const handleAddGroup = async () => {
  try {
    const { value } = await ElMessageBox.prompt('请输入分组名称', '添加分组', {
      confirmButtonText: t('common.ok'),
      cancelButtonText: t('common.cancel'),
      inputPattern: /\S+/,
      inputErrorMessage: '分组名称不能为空'
    })
    if (!value?.trim()) return
    groups.value.push({
      id: genId('group'),
      name: value.trim(),
      fields: []
    })
  } catch {
    // 用户取消
  }
}

/** 删除分组 */
const handleDeleteGroup = async (groupId: string) => {
  const group = groups.value.find((g) => g.id === groupId)
  if (!group) return
  if (group.fields.some((f) => f.system)) {
    message.warning('分组内存在系统字段，无法删除')
    return
  }
  try {
    await message.delConfirm(`确认删除分组「${group.name}」？`)
    groups.value = groups.value.filter((g) => g.id !== groupId)
  } catch {}
}

/** 添加字段 */
const handleAddField = (groupId: string) => {
  const group = groups.value.find((g) => g.id === groupId)
  if (!group) return
  const field: FormField = {
    id: genId('field'),
    fieldKey: `custom_${Date.now()}`,
    label: '',
    fieldType: 'input',
    required: false,
    unique: false,
    placeholder: '',
    defaultValue: '',
    min: 1,
    max: 255,
    enabled: true,
    system: false
  }
  group.fields.push(field)
}

/** 删除字段 */
const handleDeleteField = async (_groupId: string, field: FormField, index: number) => {
  try {
    await message.delConfirm(`确认删除字段「${field.label || '未命名'}」？`)
    const group = groups.value.find((g) => g.id === _groupId)
    if (!group) return
    group.fields.splice(index, 1)
  } catch {}
}

/** 打开移动分组 */
const openMoveDialog = (groupId: string, _field: FormField, index: number) => {
  if (groups.value.length < 2) {
    message.warning('请先添加其他分组')
    return
  }
  moveContext.groupId = groupId
  moveContext.fieldIndex = index
  moveDialogRef.value?.open()
}

/** 确认移动 */
const handleMoveConfirm = (targetGroupId: string) => {
  const source = groups.value.find((g) => g.id === moveContext.groupId)
  const target = groups.value.find((g) => g.id === targetGroupId)
  if (!source || !target || moveContext.fieldIndex < 0) return
  const [field] = source.fields.splice(moveContext.fieldIndex, 1)
  if (!field) return
  target.fields.push(field)
  message.success('移动成功')
}

/** 预览 */
const handlePreview = (group: FormGroup) => {
  previewGroup.value = group
  previewVisible.value = true
}

/** 保存 */
const handleSave = async () => {
  for (const group of groups.value) {
    if (!group.name.trim()) {
      message.warning('存在未命名分组，请先完善')
      return
    }
    for (const field of group.fields) {
      if (!field.label.trim()) {
        message.warning(`分组「${group.name}」存在未命名字段`)
        return
      }
    }
  }
  saveLoading.value = true
  try {
    await CrmFormApi.saveFormConfig(currentModule.value, groups.value)
    message.success('保存成功')
  } finally {
    saveLoading.value = false
  }
}

/** 恢复默认 */
const handleReset = async () => {
  try {
    if (import.meta.env.VITE_IS_DEMO) {
      message.warning('演示模式，无法恢复默认配置')
      return
    }
    await message.confirm('确认恢复为默认配置？当前配置将被覆盖')
    loading.value = true
    groups.value = await CrmFormApi.resetFormConfig(currentModule.value)
    message.success('已恢复默认配置')
  } catch {
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadConfig()
})
</script>

<style scoped>
.page-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 16px;
  flex-wrap: wrap;
}

.header-left {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.mb-8px {
  margin-bottom: 8px;
}

.page-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.page-desc {
  font-size: 13px;
  color: var(--el-text-color-secondary);
}

.header-right {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.preview-required {
  color: var(--el-color-danger);
  font-size: 12px;
}
</style>
