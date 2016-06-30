export default class User {
    static create(email, password, next) {
        serverRequest.put({
            url: '/users',
            json: {
                email: email,
                password: password
            }
        }, (error, response, data) => {
            if (error || response.statusCode !== 200) {
                return next(error || data || response.statusCode);
            }

            next(null, data);
        });
    }

    static auth(email, password, next) {
        serverRequest.post({
            url: '/users/auth',
            json: {
                email: email,
                password: password
            }
        }, (error, response, data) => {
            if (error || response.statusCode !== 200) {
                return next(error || data || response.statusCode);
            }

            CurrentUserStore.login(data.user, data.accessToken);

            next(null, data);
        });
    }
}
