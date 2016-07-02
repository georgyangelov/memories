import React from 'react';
import {Link} from 'react-router';
import classNames from 'classnames';
import _ from 'lodash';
import request from 'superagent';
import Dropzone from 'react-dropzone';
import constants from './config/constants.js';

global.React = React;
global.Link = Link;
global.cx = classNames;
global._ = _;
global.request = request;
global.Dropzone = Dropzone;
global.Constants = constants;

function requireGlobally(namespace, requireContext) {
    requireContext.keys().forEach(file => {
        var component = requireContext(file);

        if (component.__esModule && component.default.name) {
            var namespaceObject = global;

            if (namespace) {
                global[namespace] = global[namespace] || {};
                namespaceObject = global[namespace];
            }

            namespaceObject[component.default.name] = component.default;
        }
    });
}

function requireAllStyles() {
    var requireContext = require.context('.', true, /\.scss?$/);

    requireContext.keys().forEach(requireContext);
}

requireGlobally(null, require.context('./lib', true, /\.jsx?$/));
requireGlobally(null, require.context('./components', true, /\.jsx?$/));
requireGlobally(null, require.context('./services', true, /\.jsx?$/));
requireGlobally(null, require.context('./stores', true, /\.jsx?$/));
requireGlobally('views', require.context('./views', true, /\.jsx?$/));
requireAllStyles();

CurrentUserStore.loadStoredToken();

require('./routes.jsx');
