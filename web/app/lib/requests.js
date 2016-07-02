export default class Requests {
    static defaults(request) {
        if (request.url[0] === '/') {
            request.url = Constants.API_URL + request.url;
        }

        return request.accept('json');
    }

    static auth(request) {
        return request.use(Requests.defaults)
                      .set('Authorization', CurrentUserStore.get('accessToken'));
    }

    static response(next, notifyCalls) {
        return (error, response) => {
            if (error || !response.ok) {
                return next(response.body || error || 'unknown_error', null, response);
            }

            next(null, response.body, response);

            _.each(notifyCalls || [], _.apply);
        };
    }
}
