<script setup>
import { ref, computed } from "vue";
import { useRoute } from "vue-router";
import { AlignJustify, X, Bell } from "lucide-vue-next";

const isMenuOpen = ref(false);
const route = useRoute();

const navItems = [
  { id: "home", name: "Home", path: "/" },
  { id: "about", name: "About", path: "/about" },
  { id: "features", name: "Features", path: "/features" },
  { id: "security", name: "Security", path: "/security" },
  { id: "contacts", name: "Contacts", path: "/contacts" },
];

const toggleMenu = () => {
  isMenuOpen.value = !isMenuOpen.value;
};

const isActive = (path) => {
  return route.path === path;
};
</script>

<template>
  <!-- Mobile Menu Toggle Button -->
  <div class="border border-red-500">
    <div>
      <p>BeanCart</p>
    </div>
    <div>
      <div>
        <Bell />
      </div>
    <button @click="toggleMenu" class="md:hidden  z-40">
      <AlignJustify v-if="!isMenuOpen" />
    </button>
  </div>
  </div>

  <!-- Black Overlay -->
  <Transition name="overlay">
    <div class="absolute bg-black/50 inset-0 z-20" @click="toggleMenu" v-if="isMenuOpen"></div>
  </Transition>
  <!-- Mobile Navigation Menu -->
  <Transition name="sidebar">
    <div v-if="isMenuOpen" class="md:hidden fixed inset-0 z-30">

      <!-- Sidebar container -->
      <div class="bg-indigo-100 w-[70%] h-screen absolute right-0 top-0 z-30 pt-16 px-5 overflow-hidden">
        <Transition name="title-animation" appear>
          <div class="absolute top-2 font-bold text-2xl text-indigo-400">
            <h2>BeanCart</h2>
          </div>
        </Transition>
        <div class="absolute top-2 right-2" @click="toggleMenu">
          <X class="w-8 h-7 text-indigo-300" />
        </div>

        <div class="h-[250px] w-[250px] bg-indigo-200 rounded-full absolute -top-[125px] -left-[150px] -z-10"></div>

        <TransitionGroup name="list" tag="ul" class="w-full" appear>
          <li v-for="(item, index) in navItems" :key="item.id" :style="{ '--stagger-delay': `${index * 0.1}s` }">
            <router-link :to="item.path"
              class="flex px-4 py-3 w-full mt-5 rounded-sm shadow-sm font-semibold text-lg text-gray-500" :class="isActive(item.path) ? 'bg-indigo-300' : 'bg-indigo-300/30'
                " @click="toggleMenu">
              {{ item.name }}
            </router-link>
          </li>
        </TransitionGroup>

        <Transition name="slide-fade" appear>
          <button class="absolute bottom-24 px-4 py-3 bg-indigo-400 rounded-sm w-[80%] text-center shadow-md">
            <p class="font-semibold text-xl text-white">Get Started</p>
          </button>
        </Transition>

        <div class="absolute h-[700px] w-[700px] bg-indigo-200 rounded-full -z-10 -bottom-[200px] -left-[50px]"></div>
        <div class="bg-indigo-300 h-[100px] w-[100px] rounded-full absolute bottom-[200px]"></div>
        <div class="bg-indigo-300 h-[50px] w-[50px] rounded-full absolute bottom-[200px] right-[50px]"></div>
        <div class="bg-indigo-300 h-[40px] w-[40px] rounded-full absolute bottom-[300px] right-[25px]"></div>
      </div>
    </div>
  </Transition>

  <!-- Desktop Navigation -->
  <nav class="hidden md:flex justify-center py-4 bg-indigo-50">
    <ul class="flex space-x-6">
      <li v-for="item in navItems" :key="item.id">
        <router-link :to="item.path" class="px-4 py-2 rounded-md font-medium transition-colors" :class="isActive(item.path)
          ? 'bg-indigo-300 text-indigo-900'
          : 'hover:bg-indigo-200'
          ">
          {{ item.name }}
        </router-link>
      </li>
    </ul>
  </nav>

  <!-- Content Area -->
  <router-view />
</template>

<style>
/* Sidebar Animation */
.sidebar-enter-active,
.sidebar-leave-active {
  transition: transform 0.4s ease, opacity 0.4s ease;
}

.sidebar-enter-from {
  transform: translateX(100%);
  opacity: 0;
}

.sidebar-enter-to {
  transform: translateX(0%);
  opacity: 1;
}

.sidebar-leave-from {
  transform: translateX(0%);
  opacity: 1;
}

.sidebar-leave-to {
  transform: translateX(100%);
  opacity: 0;
}

/* Overlay Fade Animation */
.overlay-enter-active,
.overlay-leave-active {
  transition: opacity 0.1s ease;
}

.overlay-enter-from,
.overlay-leave-to {
  opacity: 0;
}

.overlay-enter-to,
.overlay-leave-from {
  opacity: 0.5;
}

.list-enter-active {
  transition: all 0.5s ease;
  transition-delay: var(--stagger-delay, 0s);
}

.list-enter-from,
.list-leave-to {
  opacity: 0;
  transform: translateX(30px);
}

/* Title animations */
.title-animation-enter-active,
.title-animation-leave-active {
  transition: all 0.4s ease-in;
}

.title-animation-enter-from,
.title-animation-leave-to {
  opacity: 0;
  transform: translateX(20px);
}

/* Button animations */
.slide-fade-enter-active,
.slide-fade-leave-active {
  transition: all 0.4s ease-in;
}

.slide-fade-enter-from,
.slide-fade-leave-to {
  opacity: 0;
  transform: translateY(20px);
}
</style>
