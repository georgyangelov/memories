import {GoogleMapLoader, GoogleMap, Marker} from "react-google-maps";

export default class ImageMap extends React.Component {
    constructor(...args) {
        super(...args);

        this.state = {image: null};
    }

    componentDidMount() {
        Image.get(this.props.params.id, (error, image) => {
            if (error) {
                console.error('Cannot load image', error);
                return;
            }

            this.setState({image: image});
        });
    }

    render() {
        var image = this.state.image;

        if (!image) {
            return null;
        }

        return <div className="image-map-view">
            <iframe frameborder="0" src={this.generateMapUrl()} allowfullscreen></iframe>
        </div>;
    }

    generateMapUrl() {
        var image = this.state.image;

        return 'https://www.google.com/maps/embed/v1/place' +
               '?key=' + Constants.MAPS_API_KEY +
               '&q=' + image.coordinates.y + ',' + image.coordinates.x;
    }
}
