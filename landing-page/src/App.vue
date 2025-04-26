<script setup>
import Navbar from "./components/Navbar.vue";
import Hero from "../src/sections/Hero.vue";
</script>

<template>
  <div class="">
    <!-- <div class="bg-indigo-200 absolute inset-0 -z-10"></div> -->
     <div class="bg-gradient-to-t from-indigo-200 to-white inset-0 absolute -z-20 hidde md:block"></div>
    <div class="sticky top-2 md:top-4 z-4">
      <Navbar />
    </div>
    <div class="max-w-screen-xl mx-auto">
      <Hero />
    </div>
    <main class="max-w-screen-xl mx-auto"></main>
  </div>
</template>

<style scoped></style>
