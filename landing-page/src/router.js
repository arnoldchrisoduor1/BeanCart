import { createRouter, createWebHistory } from 'vue-router';

// Import your page components
// Example:
// import Home from './views/Home.vue';
// import About from './views/About.vue';

// For this example, we'll use placeholder components
const Home = { template: '<div>Home Page</div>' };
const About = { template: '<div>About Page</div>' };
const Features = { template: '<div>Features Page</div>' };
const Security = { template: '<div>Security Page</div>' };
const Contacts = { template: '<div>Contacts Page</div>' };

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/about',
    name: 'About',
    component: About
  },
  {
    path: '/features',
    name: 'Features',
    component: Features
  },
  {
    path: '/security',
    name: 'Security',
    component: Security
  },
  {
    path: '/contacts',
    name: 'Contacts',
    component: Contacts
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;