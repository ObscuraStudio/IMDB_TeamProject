import axios from "axios";
import {useState} from "react";
import type {OmdbMovieResponse} from "../types/Movie.ts";

const FAVOURITES_URI = "/api/favourites";
const secureUrl = new URL(FAVOURITES_URI, window.location.origin);

export function useFavourites() {
    const [favourites, setFavourites] = useState<OmdbMovieResponse[]>([]);

    const loadFavourites = () => {
        axios.get<OmdbMovieResponse[]>(FAVOURITES_URI)
            .then(response => setFavourites(response.data))
            .catch(() => setFavourites([]));
    };

    // Each mutation returns the updated list from the backend, so state stays in sync.
    const addFavourite = (movie: OmdbMovieResponse) =>
        axios.post<OmdbMovieResponse[]>(secureUrl.href, movie)
            .then(response => setFavourites(response.data));

    const removeFavourite = (imdbId: string) => {
        // Validate the id against an allowlist before putting it in the URL, so
        // tainted input can't forge a different request (Sonar S8476).
        if (!/^[a-zA-Z0-9]+$/.test(imdbId)) {
            return Promise.reject(new Error("Invalid imdbId"));
        }
        const url = new URL(`${FAVOURITES_URI}/${encodeURIComponent(imdbId)}`, window.location.origin);
        return axios.delete<OmdbMovieResponse[]>(url.href)
            .then(response => setFavourites(response.data));
    };

    return {favourites, loadFavourites, addFavourite, removeFavourite};
}
