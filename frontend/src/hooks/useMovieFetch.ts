import {useState} from "react";
import {type OmdbMovieResponse} from "../types/Movie.ts";
import axios from "axios";

export function useMovieFetch() {
    // This hook helps with conditional rendering
    const [isSubmitButtonClicked, setIsSubmitButtonClicked] = useState<boolean>(false);

    const [movie, setMovie] = useState<OmdbMovieResponse>({Title: "", Year: ""});
    const [isMovieValid, setIsMovieValid] = useState<boolean>(true);

    function submit(queryURI: string) {
        setIsSubmitButtonClicked(true);
        axios.get(queryURI)
            .then((response) => {
                setMovie(response.data);
                setIsMovieValid(true);
            })
            .catch(() => setIsMovieValid(false));
    }

    return {isSubmitButtonClicked, isMovieValid, movie, submit};
}