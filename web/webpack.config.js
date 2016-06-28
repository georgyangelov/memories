var webpack = require('webpack'),
    path = require('path'),

    BUILD_DIR = path.resolve(__dirname, 'public'),
    APP_DIR = path.resolve(__dirname, 'app');

module.exports = {
    entry: [
        'webpack-dev-server/client?http://0.0.0.0:3000',
        'webpack/hot/only-dev-server',
        APP_DIR + '/index.jsx'
    ],
    plugins: [
        new webpack.HotModuleReplacementPlugin()
    ],
    module: {
        loaders: [
            {
                test: /\.jsx?/,
                include: APP_DIR,
                loaders: ['react-hot', 'babel']
            },
            {
                test: /\.scss$/,
                loaders: ['style', 'css?sourceMap', 'sass?sourceMap']
            }
        ]
    },
    output: {
        path: BUILD_DIR,
        filename: 'bundle.js'
    }
};
