<template>
  <el-form-item :label="field.label" :prop="field.fieldKey">
    <!-- 地图选址 -->
    <template v-if="field.fieldKey === 'addressStr'">
      <el-input
        :model-value="formData.addressStr"
        :placeholder="field.placeholder || '请选择地址'"
        readonly
      >
        <template #append>
          <el-button @click="emit('open-map')">选择地址</el-button>
        </template>
      </el-input>
    </template>

    <!-- 客户标签特殊交互 -->
    <template v-else-if="field.fieldKey === 'tags'">
      <el-input
        :model-value="formData.tags"
        :placeholder="field.placeholder || '请选择标签'"
        readonly
        @click="emit('open-tags')"
      />
      <div style="margin-top: 2px" class="gap-2">
        <el-tag
          v-for="(tag, idx) in tagsArr"
          :key="idx"
          closable
          type="danger"
          class="mr-1"
          @close="emit('remove-tag', idx)"
        >
          {{ tag }}
        </el-tag>
      </div>
    </template>

    <el-input
      v-else-if="field.fieldType === 'input'"
      v-model="fieldValue"
      :placeholder="field.placeholder"
      :minlength="field.min ?? undefined"
      :maxlength="field.max ?? undefined"
    />

    <el-input
      v-else-if="field.fieldType === 'textarea'"
      v-model="fieldValue"
      type="textarea"
      :rows="2"
      :placeholder="field.placeholder"
      :maxlength="field.max ?? undefined"
    />

    <el-input-number
      v-else-if="field.fieldType === 'number'"
      v-model="fieldValue"
      class="w-full"
      :min="field.min ?? undefined"
      :max="field.max ?? undefined"
      :placeholder="field.placeholder"
    />

    <el-select
      v-else-if="field.fieldType === 'select'"
      v-model="fieldValue"
      clearable
      class="w-full"
      :placeholder="field.placeholder"
    >
      <el-option
        v-for="dict in dictOptions"
        :key="String(dict.value)"
        :label="dict.label"
        :value="dict.value"
      />
    </el-select>

    <el-radio-group v-else-if="field.fieldType === 'radio'" v-model="fieldValue">
      <el-radio v-for="dict in dictOptions" :key="String(dict.value)" :value="dict.value">
        {{ dict.label }}
      </el-radio>
    </el-radio-group>

    <el-checkbox-group v-else-if="field.fieldType === 'checkbox'" v-model="checkboxValue">
      <el-checkbox v-for="dict in dictOptions" :key="String(dict.value)" :value="dict.value">
        {{ dict.label }}
      </el-checkbox>
    </el-checkbox-group>

    <el-date-picker
      v-else-if="field.fieldType === 'datetime'"
      v-model="fieldValue"
      type="datetime"
      value-format="x"
      class="w-full"
      :placeholder="field.placeholder || '选择日期时间'"
    />

    <el-input v-else v-model="fieldValue" :placeholder="field.placeholder" />
  </el-form-item>
</template>

<script setup lang="ts">
import type { FormField } from '@/views/crm/crmform/types'
import { AREA_CASCADER_DICT } from '@/views/crm/crmform/utils/formConfig'
import { getIntDictOptions, getStrDictOptions } from '@/utils/dict'

const props = defineProps<{
  field: FormField
  formData: Record<string, any>
  tagsArr?: string[]
}>()

const emit = defineEmits<{
  'update:formData': [value: Record<string, any>]
  'open-tags': []
  'remove-tag': [index: number]
  'open-map': []
}>()

const fieldValue = computed({
  get: () => props.formData[props.field.fieldKey],
  set: (val: any) => {
    emit('update:formData', { ...props.formData, [props.field.fieldKey]: val })
  }
})

/** checkbox 组：存数组时直接用；逗号字符串则拆分 */
const checkboxValue = computed({
  get: () => {
    const raw = props.formData[props.field.fieldKey]
    if (Array.isArray(raw)) return raw
    if (raw === undefined || raw === null || raw === '') return []
    return String(raw)
      .split(',')
      .map((s) => {
        const n = Number(s)
        return Number.isNaN(n) ? s : n
      })
  },
  set: (val: any[]) => {
    emit('update:formData', {
      ...props.formData,
      [props.field.fieldKey]: val
    })
  }
})

const dictOptions = computed(() => {
  const dictType = props.field.dictType
  if (!dictType || dictType === AREA_CASCADER_DICT) return []
  const intOpts = getIntDictOptions(dictType)
  if (intOpts.length) return intOpts
  return getStrDictOptions(dictType)
})
</script>
