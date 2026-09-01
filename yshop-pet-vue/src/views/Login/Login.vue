<template>
  <div
   style="background-color:#f0f2f5"
    :class="prefixCls"
    class="h-[100%] relative <xl:bg-v-dark "
  >
    <div class="relative h-full flex mx-auto ">
      <div class="w-[100%] ">
        <div class="h-[100%] flex flex-col justify-center items-center">
          
          <Transition appear enter-active-class="animate__animated animate__bounceInRight">
            
            <div
              class="rounded-xl bg-white  w-[30%] "
            >
              <div class="flex items-center justify-center pt-20px <2xl:hidden">
                <img alt="" class="w-48px h-48px mr-10px" src="@/assets/imgs/logo.png" />
                <span class="text-20px font-bold">{{ underlineToHump(appStore.getTitle) }}</span>
              </div>
              <!-- 账号登录 -->
              <LoginForm class="px-30px py-20px  h-auto m-auto <xl:(rounded-3xl light:bg-white)" />
              <div class="text-center pb-30px text-gray-5 <2xl:hidden">温馨提示：当前登陆超级管理员账号哦</div>
            </div>
          </Transition>
    
        </div>
        
      </div>
    </div>
  </div>
</template>
<script lang="ts" name="Login" setup>
import { underlineToHump } from '@/utils'

import { useDesign } from '@/hooks/web/useDesign'
import { useAppStore } from '@/store/modules/app'
//import { ThemeSwitch } from '@/layout/components/ThemeSwitch'
//import { LocaleDropdown } from '@/layout/components/LocaleDropdown'

import { LoginForm } from './components'
// import { LoginForm, MobileForm, QrCodeForm, RegisterForm, SSOLoginVue } from './components'

const { t } = useI18n()
const appStore = useAppStore()
const { getPrefixCls } = useDesign()
const prefixCls = getPrefixCls('login')
appStore.setIsDark(false)
</script>

<style lang="scss" scoped>
$prefix-cls: #{$namespace}-login;

.#{$prefix-cls} {
  &__left {
    &::before {
      position: absolute;
      top: 0;
      left: 0;
      z-index: -1;
      width: 100%;
      height: 100%;
      background-image: url('@/assets/svgs/login-bg.svg');
      background-position: center;
      background-repeat: no-repeat;
      content: '';
    }
  }
}
</style>
