<script setup>

import { Truck, ShieldPlus, Fingerprint, ReceiptEuro} from 'lucide-vue-next';

</script>

<template>
    <!-- Mobile View -->
    <div class="relative overflow-hidden -mt-7 -z-10 md:hidden">
        <div class="flex flex-col mt-20 w-[95%] mx-auto">
            <p class="font-bold text-xl text-indigo-400 border-2 rounded-full px-4 py-1 w-fit mb-5 tracking-tight">BeanCart</p>
            <h1 class="font-extrabold text-4xl bg-gradient-to-r from-indigo-500 to-indigo-900 text-transparent bg-clip-text mb-5 tracking-tight">Buy and Sell Online <br/> Fast and Securely.</h1>
            <p class="font-semibold text-xl text-indigo-900">Commerce modernized and tailored to today's needs.</p>
        </div>
      <div class="w-[600px] h-[600px] rounded-full bg-white shadow-[inset_0_0_20px_theme('colors.indigo.300')]  rotate-slow">
        <div class="absolute top-[30px] left-[210px] w-[200px] h-[200px] rounded-full bg-white shadow-[inset_0_0_30px_theme('color.indigo.400')] flex items-center justify-center">
            <div class="spin-counter">
                <div>
                    <Truck class="h-16 w-16" />
                </div>
                <div>
                    <p class="font-semibold text-center">Fast <br/> Deliveries</p>
                </div>
            </div>
        </div>
        <div class="absolute top-[200px] left-[30px] w-[200px] h-[200px] rounded-full bg-white shadow-[inset_0_0_30px_theme('color.indigo.400')] flex items-center justify-center">
            <div class="spin-counter">
                <div>
                    <ShieldPlus class="h-16 w-16"/>
                </div>
                <div>
                    <p class="font-semibold text-center">Customer <br/> Protection</p>
                </div>
            </div>
        </div>
        <div class="absolute top-[370px] left-[210px] w-[200px] h-[200px] rounded-full bg-white shadow-[inset_0_0_30px_theme('color.indigo.400')] flex items-center justify-center">
            <div class="text-center spin-counter">
                <div>
                    <Fingerprint class="h-16 w-16"/>
                </div>
                <div>
                    <p class="font-semibold text-center">Secure <br/> Payments</p>
                </div>
            </div>
        </div>
        <div class="absolute top-[200px] left-[380px] w-[200px] h-[200px] rounded-full bg-white shadow-[inset_0_0_30px_theme('color.indigo.400')] flex items-center justify-center">
            <div class="spin-counter">
                <div>
                    <ReceiptEuro class="h-16 w-16"/>
                </div>
                <div>
                    <p class="font-semibold text-center">Proper <br/> Invoicing</p>
                </div>
            </div>
        </div>
      </div>
      
    </div>

    <div class="relative overflow-hidden -mt-7 -z-10 hidden md:flex items-center md:justify-around">
        <div class="flex flex-col">
            <p class="font-bold text-3xl text-indigo-400 border-2 rounded-full px-4 py-2 w-fit mb-5 tracking-tight">BeanCart</p>
            <h1 class="font-extrabold text-6xl bg-gradient-to-r from-indigo-500 to-indigo-900 text-transparent bg-clip-text mb-5 tracking-tight">Buy and Sell Online <br/> Fast and Securely.</h1>
            <p class="font-semibold text-xl text-indigo-900">Commerce modernized and tailored to today's needs.</p>
        </div>
      <div class="w-[600px] h-[600px] rounded-full bg-white shadow-[inset_0_0_20px_theme('colors.indigo.300')]  rotate-slow">
        <div class="absolute top-[30px] left-[210px] w-[200px] h-[200px] rounded-full bg-white shadow-[inset_0_0_30px_theme('color.indigo.400')] flex items-center justify-center">
            <div class="spin-counter">
                <div>
                    <Truck class="h-16 w-16" />
                </div>
                <div>
                    <p class="font-semibold text-center">Fast <br/> Deliveries</p>
                </div>
            </div>
        </div>
        <div class="absolute top-[200px] left-[30px] w-[200px] h-[200px] rounded-full bg-white shadow-[inset_0_0_30px_theme('color.indigo.400')] flex items-center justify-center">
            <div class="spin-counter">
                <div>
                    <ShieldPlus class="h-16 w-16"/>
                </div>
                <div>
                    <p class="font-semibold text-center">Customer <br/> Protection</p>
                </div>
            </div>
        </div>
        <div class="absolute top-[370px] left-[210px] w-[200px] h-[200px] rounded-full bg-white shadow-[inset_0_0_30px_theme('color.indigo.400')] flex items-center justify-center">
            <div class="text-center spin-counter">
                <div>
                    <Fingerprint class="h-16 w-16"/>
                </div>
                <div>
                    <p class="font-semibold text-center">Secure <br/> Payments</p>
                </div>
            </div>
        </div>
        <div class="absolute top-[200px] left-[380px] w-[200px] h-[200px] rounded-full bg-white shadow-[inset_0_0_30px_theme('color.indigo.400')] flex items-center justify-center">
            <div class="spin-counter">
                <div>
                    <ReceiptEuro class="h-16 w-16"/>
                </div>
                <div>
                    <p class="font-semibold text-center">Proper <br/> Invoicing</p>
                </div>
            </div>
        </div>
      </div>
    </div>
  </template>
  
  <style>
  @keyframes spin-reverse {
    from {
      transform: rotate(0deg);
    }
    to {
      transform: rotate(-360deg);
    }
  }
  
  .rotate-slow {
    animation: spin-reverse 50s linear infinite;
    transform-origin: center;
  }

  @keyframes spin-forward {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.spin-counter {
  animation: spin-forward 50s linear infinite;
}

  </style>
  