const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');

// Function to generate multiple page configurations
const generatePages = () => {
  const pages = [
    { name: 'login', entry: './login.ts' },
    { name: 'app', entry: './app.ts' },
  ];

  return pages.map(page =>
    new HtmlWebpackPlugin({
      template: `./${page.name}.html`,
      filename: `${page.name}.html`,
      chunks: [page.name]
    })
  );
};

module.exports = {
  mode: 'development',
  entry: {
    login: './login.ts',
    app: './app.ts',
  },
  output: {
    filename: '[name].[contenthash].js',
    path: path.resolve(__dirname, 'dist'),
  },
  resolve: {
    extensions: ['.ts', '.js']
  },
  module: {
    rules: [
      {
        test: /\.ts$/,
        use: 'ts-loader',
        exclude: /node_modules/
      }
    ]
  },
  plugins: [
    new CleanWebpackPlugin(),
    ...generatePages()
  ],
  devServer: {
    static: './dist',
    port: 8080
  }
};
