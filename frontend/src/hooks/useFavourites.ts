import axios from "axios";
import {useState} from "react";
import type {OmdbMovieResponse} from "../types/Movie.ts";

const FAVOURITES_URI = "/api/favourites";

export function useFavourites() {
    const [favourites, setFavourites] = useState<OmdbMovieResponse[]>([]);

    const loadFavourites = () => {
        axios.get<OmdbMovieResponse[]>(FAVOURITES_URI)
            .then(response => setFavourites(response.data))
            .catch(() => setFavourites([]));
    };

    // Each mutation returns the updated list from the backend, so state stays in sync.
    const addFavourite = (movie: OmdbMovieResponse) =>
        axios.post<OmdbMovieResponse[]>(FAVOURITES_URI, movie)
            .then(response => setFavourites(response.data));

    const removeFavourite = (imdbId: string) =>
        axios.delete<OmdbMovieResponse[]>(`${FAVOURITES_URI}/${imdbId}`)
            .then(response => setFavourites(response.data));

    return {favourites, loadFavourites, addFavourite, removeFavourite};
}
