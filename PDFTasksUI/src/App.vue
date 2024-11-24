<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <div class="app-container">
    <header class="top-section">
      <h1>PDFTasks</h1>
    </header>

    <nav class="bottom-section">
      <RouterLink to="/" class="nav-link">Home</RouterLink>
      <RouterLink to="/merge" class="nav-link">Merge</RouterLink>
      <RouterLink to="/contact" class="nav-link">Contact</RouterLink>
    </nav>
    <RouterView />
  </div>


</template>

<style scoped>
/* General Styles */
body {
  font-family: Arial, sans-serif;
  margin: 0;
  padding: 0;
  background-color: #f9f9f9;
}

/* App Container */
.app-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  min-height: 100vh;
  text-align: center;
  padding: 20px;
}

/* Top Section */
.top-section {
  background-color: #007bff;
  color: white;
  padding: 20px;
  border-radius: 8px;
  width: 100%;
  max-width: 800px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px; /* Added space between top and bottom sections */
}
.top-section h1 {
  margin: 0;
  font-size: 2.5rem;
}
.top-section p {
  margin: 10px 0 0;
  font-size: 1rem;
}

/* Bottom Section */
.bottom-section {
  display: flex;
  gap: 15px;
  margin-top: 0;
  padding: 10px 0;
  width: 100%;
  max-width: 800px;
}
.nav-link {
  text-decoration: none;
  color: #007bff;
  font-size: 1.2rem;
  border: 2px solid #007bff;
  padding: 8px 16px;
  border-radius: 4px;
  transition: all 0.3s ease;
}
.nav-link:hover {
  background-color: #007bff;
  color: white;
}
.RouterView {
  width: 100%;
  max-width: 800px;
  padding: 20px;
  background-color: white;
  margin-top: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}
</style>