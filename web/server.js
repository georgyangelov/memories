var webpack = require('webpack'),
    WebpackDevServer = require('webpack-dev-server'),
    config = require('./webpack.config');

new WebpackDevServer(webpack(config), {
    publicPath: config.output.publicPath,
    contentBase: 'public/',
    hot: true,
    historyApiFallback: true,
    colors: true,
    stats: 'errors-only'
}).listen(3000, '0.0.0.0', function(err, result) {
    if (err) {
        return console.log(err);
    }

    console.log('Listening at http://localhost:3000/');
});
