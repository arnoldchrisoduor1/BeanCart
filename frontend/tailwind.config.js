/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./src/**/*.{html,ts}",
    "./src/**/*.component.html"
  ],
  theme: {
    extend: {
      fontFamily: {
        // Set 'Outfit' as the default sans font
        sans: ['Outfit', 'sans-serif'],
      },
    },
  },
  plugins: [
    function({ addComponents }) {
      addComponents({
        '.header-title': {
          '@apply text-3xl font-bold tracking-tighter text-indigo-900': '',
        }
      })
    }
  ],
  safelist: [
    'bg-indigo-500',
    'hover:bg-indigo-600',
    'bg-indigo-300',
    'text-white',
    'cursor-not-allowed',
  ],
}