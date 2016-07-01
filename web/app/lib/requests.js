export default class Requests {
    static apiNoAuth(options, next) {
        var requestOptions = _.merge({
            baseUrl: 'http://localhost:8080/',
            headers: {
                'Accept': 'application/json'
            },
            json: true
        }, options);

        return request(requestOptions, this.responseHandler(next));
    }

    static get(url, data, next) {
        if (typeof next === 'undefined') {
            next = data;
            data = null;
        }

        this.apiNoAuth({
            url: url,
            method: 'GET',
            headers: {
                'Authorization': CurrentUserStore.get('accessToken')
            },
            qs: data
        }, next);
    }

    static put(url, data, next) {
        if (typeof next === 'undefined') {
            next = data;
            data = null;
        }

        this.apiNoAuth({
            url: url,
            method: 'PUT',
            headers: {
                'Authorization': CurrentUserStore.get('accessToken')
            },
            json: data
        }, next);
    }

    static post(url, data, next) {
        if (typeof next === 'undefined') {
            next = data;
            data = null;
        }

        this.apiNoAuth({
            url: url,
            method: 'POST',
            headers: {
                'Authorization': CurrentUserStore.get('accessToken')
            },
            json: data
        }, next);
    }

    static delete(url, data, next) {
        if (typeof next === 'undefined') {
            next = data;
            data = null;
        }

        this.apiNoAuth({
            url: url,
            method: 'DELETE',
            headers: {
                'Authorization': CurrentUserStore.get('accessToken')
            },
            json: data
        }, next);
    }

    static responseHandler(next) {
        return (error, response, data) => {
            if (error || response.statusCode !== 200) {
                return next(error || data || response.statusCode || 'unknown_error');
            }

            next(null, data);
        };
    }
}
