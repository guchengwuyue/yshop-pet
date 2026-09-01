<template>
  <el-table :data="fields" border class="field-config-table" size="small">
    <el-table-column label="字段名称" min-width="130">
      <template #default="{ row }">
        <el-input v-model="row.label" placeholder="请输入字段名称" size="small" />
      </template>
    </el-table-column>

    <el-table-column label="字段类型" min-width="140">
      <template #default="{ row }">
        <el-select
          v-model="row.fieldType"
          placeholder="请选择"
          size="small"
          class="w-full"
          @change="onFieldTypeChange(row)"
          :disabled="row.system" 
        >
          <el-option
            v-for="opt in FIELD_TYPE_OPTIONS"
            :key="opt.value"
            :label="opt.label"
            :value="opt.value"
          />
        </el-select>
      </template>
    </el-table-column>

    <el-table-column label="关联字典" min-width="140">
      <template #default="{ row }">
        <el-select
          v-if="supportsDict(row.fieldType)"
          v-model="row.dictType"
          clearable
          placeholder="请选择字典"
          size="small"
          class="w-full"
          :disabled="row.system" 
        >
          <el-option
            v-for="opt in DICT_TYPE_OPTIONS"
            :key="opt.value"
            :label="opt.label"
            :value="opt.value"
          />
        </el-select>
        <span v-else class="text-secondary">-</span>
      </template>
    </el-table-column>

    <el-table-column label="是否必填" width="100" align="center">
      <template #default="{ row }">
        <div class="flex flex-col items-center gap-4px">
          <el-switch v-model="row.required" />
          <span class="text-12px" :class="row.required ? 'text-primary' : 'text-secondary'">
            {{ row.required ? '必填' : '选填' }}
          </span>
        </div>
      </template>
    </el-table-column>

    <el-table-column label="唯一校验" width="90" align="center">
      <template #default="{ row }">
        <el-switch v-model="row.unique" />
      </template>
    </el-table-column>

    <el-table-column label="提示信息" min-width="130">
      <template #default="{ row }">
        <el-input v-model="row.placeholder" placeholder="提示信息" size="small" />
      </template>
    </el-table-column>

    <el-table-column label="默认值" min-width="120">
      <template #default="{ row }">
        <el-input v-model="row.defaultValue" placeholder="默认值" size="small" />
      </template>
    </el-table-column>

    <el-table-column label="边界值" min-width="160" align="center">
      <template #default="{ row }">
        <div v-if="supportsBoundary(row.fieldType)" class="flex items-center justify-center gap-4px">
          <el-input-number
            v-model="row.min"
            :controls="false"
            :min="0"
            placeholder="Min"
            size="small"
            class="boundary-input"
          />
          <span>-</span>
          <el-input-number
            v-model="row.max"
            :controls="false"
            :min="0"
            placeholder="Max"
            size="small"
            class="boundary-input"
          />
        </div>
        <span v-else class="text-secondary text-12px">不支持设置边界值</span>
      </template>
    </el-table-column>

    <el-table-column label="状态" width="90" align="center">
      <template #default="{ row }">
        <div class="flex flex-col items-center gap-4px">
          <el-switch v-model="row.enabled" />
          <span class="text-12px text-primary">{{ row.enabled ? '启用' : '停用' }}</span>
        </div>
      </template>
    </el-table-column>

    <el-table-column label="操作" width="140" align="center" fixed="right">
      <template #default="{ row, $index }">
        <el-button link type="primary" size="small" @click="emit('move', row, $index)">
          移动分组
        </el-button>
        <el-button link type="primary" size="small" @click="handleDelete(row, $index)">
          删除
        </el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script setup lang="ts">
import {
  DICT_TYPE_OPTIONS,
  FIELD_TYPE_OPTIONS,
  supportsBoundary,
  supportsDict,
  type FormField
} from '../types'

defineOptions({ name: 'FieldConfigTable' })

defineProps<{
  fields: FormField[]
}>()

const emit = defineEmits<{
  move: [field: FormField, index: number]
  delete: [field: FormField, index: number]
}>()

const message = useMessage()

const onFieldTypeChange = (row: FormField) => {
  if (!supportsDict(row.fieldType)) {
    row.dictType = undefined
  }
  if (!supportsBoundary(row.fieldType)) {
    row.min = null
    row.max = null
  }
}

const handleDelete = (row: FormField, index: number) => {
  if (row.system) {
    message.warning('系统字段不可删除')
    return
  }
  emit('delete', row, index)
}
</script>

<style scoped>
.field-config-table {
  width: 100%;
}

.boundary-input {
  width: 64px;
}

.text-secondary {
  color: var(--el-text-color-secondary);
}

.text-primary {
  color: var(--el-color-primary);
}
</style>
