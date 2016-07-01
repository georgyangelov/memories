export default class User {
    static findByToken(accessToken, next) {
        Requests.get(`/users/${accessToken}`, next);
    }

    static create(email, password, next) {
        Requests.apiNoAuth({
            url: '/users',
            method: 'PUT',
            json: {
                email: email,
                password: password
            }
        }, next);
    }

    static auth(email, password, next) {
        Requests.apiNoAuth({
            url: '/users/auth',
            method: 'POST',
            json: {
                email: email,
                password: password
            }
        }, next);
    }

    static deauth(accessToken, next) {
        Requests.post('/users/deauth', {
            accessToken: accessToken
        }, next);
    }
}
