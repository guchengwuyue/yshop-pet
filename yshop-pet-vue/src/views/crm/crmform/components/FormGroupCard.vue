<template>
  <div class="form-group-card">
    <div class="group-header">
      <div class="group-title">
        <span class="title-bar"></span>
        <template v-if="editing">
          <el-input
            ref="nameInputRef"
            v-model="editName"
            size="small"
            class="name-input"
            @keyup.enter="confirmEditName"
            @blur="confirmEditName"
          />
        </template>
        <span v-else class="title-text">{{ group.name }}</span>
      </div>
      <div class="group-actions">
        <el-tooltip content="预览" placement="top">
          <el-button link type="primary" @click="emit('preview', group)">
            <Icon icon="ep:view" />
          </el-button>
        </el-tooltip>
        <el-tooltip content="编辑分组名" placement="top">
          <el-button link type="primary" @click="startEditName">
            <Icon icon="ep:edit" />
          </el-button>
        </el-tooltip>
        <el-tooltip content="删除分组" placement="top">
          <el-button link type="danger" @click="handleDeleteGroup">
            <Icon icon="ep:delete" />
          </el-button>
        </el-tooltip>
      </div>
    </div>

    <FieldConfigTable
      :fields="group.fields"
      @move="(field, index) => emit('move-field', group.id, field, index)"
      @delete="(field, index) => emit('delete-field', group.id, field, index)"
    />

    <div class="add-field">
      <el-button link type="primary" @click="emit('add-field', group.id)">
        <Icon icon="ep:plus" class="mr-4px" />
        添加字段
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { FormField, FormGroup } from '../types'
import FieldConfigTable from './FieldConfigTable.vue'

defineOptions({ name: 'FormGroupCard' })

const props = defineProps<{
  group: FormGroup
}>()

const emit = defineEmits<{
  'update:group': [group: FormGroup]
  'add-field': [groupId: string]
  'delete-field': [groupId: string, field: FormField, index: number]
  'move-field': [groupId: string, field: FormField, index: number]
  'delete-group': [groupId: string]
  preview: [group: FormGroup]
}>()

const message = useMessage()
const editing = ref(false)
const editName = ref('')
const nameInputRef = ref()

const startEditName = () => {
  editName.value = props.group.name
  editing.value = true
  nextTick(() => {
    nameInputRef.value?.focus?.()
  })
}

const confirmEditName = () => {
  if (!editing.value) return
  const name = editName.value.trim()
  if (!name) {
    message.warning('分组名称不能为空')
    return
  }
  editing.value = false
  if (name !== props.group.name) {
    emit('update:group', { ...props.group, name })
  }
}

const handleDeleteGroup = () => {
  emit('delete-group', props.group.id)
}
</script>

<style scoped>
.form-group-card {
  background: var(--el-bg-color);
  border: 1px solid var(--el-border-color-lighter);
  border-radius: 6px;
  padding: 16px;
  margin-bottom: 16px;
}

.group-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
}

.group-title {
  display: flex;
  align-items: center;
  gap: 8px;
}

.title-bar {
  display: inline-block;
  width: 3px;
  height: 16px;
  background: var(--el-color-primary);
  border-radius: 2px;
}

.title-text {
  font-size: 15px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.name-input {
  width: 200px;
}

.group-actions {
  display: flex;
  align-items: center;
  gap: 4px;
}

.add-field {
  margin-top: 12px;
  padding-left: 4px;
}
</style>
