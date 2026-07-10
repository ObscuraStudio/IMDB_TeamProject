import {useEffect} from "react";
import {useFavourites} from "../hooks/useFavourites.ts";

export default function Favourites() {
    const {favourites, loadFavourites, removeFavourite} = useFavourites();

    useEffect(() => {
        loadFavourites();
    }, []);

    return (
        <div className="favourites-page">
            <h1>My Favourites</h1>
            {favourites.length === 0 ? (
                <p className="fav-empty">No favourites yet — search for a movie and add one.</p>
            ) : (
                <ul className="fav-list">
                    {favourites.map(movie => (
                        <li key={movie.imdbID} className="fav-item">
                            {movie.Poster && movie.Poster !== "N/A" && (
                                <img className="fav-poster" src={movie.Poster} alt={movie.Title}/>
                            )}
                            <div className="fav-info">
                                <span className="fav-title">{movie.Title}</span>
                                {movie.Year && <span className="fav-year">{movie.Year}</span>}
                            </div>
                            <button
                                className="fav-remove"
                                onClick={() => movie.imdbID && removeFavourite(movie.imdbID)}
                                title="Remove from favourites"
                            >
                                ✕
                            </button>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}
