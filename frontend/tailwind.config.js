/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./src/**/*.{html,ts}",
    "./src/**/*.component.html"
  ],
  theme: {
    extend: {},
  },
  plugins: [],
  safelist: [
    // Indigo colors
    'bg-indigo-500',
    'hover:bg-indigo-600',
    'bg-indigo-300',
    
    // Text colors
    'text-white',
    
    // States
    'cursor-not-allowed',
    
    // Add other colors you might use (blue, red, etc.)
    // 'bg-blue-500',
    // 'hover:bg-blue-600',
    // 'bg-blue-300',
  ]
}