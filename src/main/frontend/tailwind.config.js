/** @type {import('tailwindcss').Config} */
const widthColumns = {};
for (let i = 1; i <= 24; i++) {
  const key = `${i}/24`;
  widthColumns[key] = `${(100 / 24) * i}%`;
}

const gridColumns = {
  ...Array.from({ length: 24 }, (_, i) => `span-${i + 1}`).reduce((acc, key) => {
    acc[key] = `span ${key.split('-')[1]} / span ${key.split('-')[1]}`;
    return acc;
  }, {})
}

module.exports = {
  content: [
    "../resources/templates/**/*.{html,js}",
    "./src/**/*.{html,js,jsx,ts,tsx,vue}"
  ],
  theme: {
    extend: {
      gridTemplateColumns: {
        '24': 'repeat(24, minmax(0, 1fr))',
      },
      gridColumn: gridColumns,
      width: widthColumns,
    },
    screens: {
      'sm': '640px',
      'md': '768px',
      'lg': '1024px',
      'xl': '1280px',
      '2xl': '1536px',
      '3xl': '1920px',
      '4xl': '2560px',
      '5xl': '3840px',
    },
  },
  plugins: [],
}
