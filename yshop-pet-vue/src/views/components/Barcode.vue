<template>
  <div v-loading="loading" class="inline-block" :style="wrapStyle">
    <img  ref="imgRef" alt="barcode" class="block max-w-full" />
  </div>
</template>
<script lang="ts" setup>
import { computed, nextTick, ref, unref, watch } from 'vue'
import JsBarcode from 'jsbarcode'
import { propTypes } from '@/utils/propTypes'

defineOptions({ name: 'Barcode' })

const props = defineProps({
  content: propTypes.string.def(''), // 条码内容
  format: propTypes.number.def('CODE39'), 
  width: propTypes.number.def(200), // 宽度
  height: propTypes.number.def(100), // 高度（仅一维码使用）
  displayValue: propTypes.bool.def(true) // 是否显示文本
})

const emit = defineEmits(['done'])

const loading = ref(true) // 加载状态
const imgRef = ref<Nullable<HTMLImageElement>>(null) // 一维码的图片引用
const wrapStyle = computed(() => {
  return {
    width: props.width + 'px'
  }
})

/** 生成条码 */
const generateBarcode = async () => {
  if (!props.content) {
    loading.value = false
    return
  }

  await nextTick()
  loading.value = true
  try {
     // 生成一维码
     JsBarcode(unref(imgRef) as HTMLImageElement, props.content, {
        format: 'CODE39',
        width: 2,
        height: props.height,
        displayValue: props.displayValue,
        margin: 10
      })
      emit('done', (unref(imgRef) as HTMLImageElement).src)
  } catch (error) {
    console.error('生成条码失败:', error)
  } finally {
    loading.value = false
  }
}

watch(
  () => [props.content, props.format],
  () => {
    if (props.content) {
      generateBarcode()
    }
  },
  {
    immediate: true
  }
)

/** 获取条码 Base64 数据 */
const getImageBase64 = (): string => {
  return (unref(imgRef) as HTMLImageElement)?.src || ''
}

defineExpose({
  getImageBase64
})
</script>
