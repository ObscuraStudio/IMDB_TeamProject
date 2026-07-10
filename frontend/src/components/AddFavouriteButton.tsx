import {useState} from "react";
import type {OmdbMovieResponse} from "../types/Movie.ts";
import {useFavourites} from "../hooks/useFavourites.ts";

type AddFavouriteButtonProps = {
    movie: OmdbMovieResponse;
};

export default function AddFavouriteButton({movie}: Readonly<AddFavouriteButtonProps>) {
    const {addFavourite} = useFavourites();
    const [added, setAdded] = useState<boolean>(false);

    // Nothing sensible to save without an id.
    if (!movie?.imdbID) {
        return null;
    }

    function handleAdd() {
        addFavourite(movie)
            .then(() => setAdded(true))
            .catch(() => setAdded(false));
    }

    return (
        <button
            className={added ? "fav-btn added" : "fav-btn"}
            onClick={handleAdd}
            disabled={added}
            title="Add to favourites"
        >
            {added ? "★ Added to favourites" : "☆ Add to favourites"}
        </button>
    );
}
