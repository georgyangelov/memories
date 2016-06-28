export default class Index extends React.Component {
    images() {
        return [
            [600, 300],
            [500, 250],
            [300, 300],
            [400, 600],
            [500, 500],
            [600, 301],
            [500, 251],
            [300, 301],
            [400, 601],
            [500, 501],
            [600, 302],
            [500, 252],
            [300, 302],
            [400, 602],
            [500, 502],
            [500, 502],
        ].map(([width, height]) => {
            return {
                width: width,
                height: height,
                url: `http://placekitten.com/${width}/${height}`
            };
        });
    }

    render() {
        return <ImageGrid images={this.images()} />;
    }
}
