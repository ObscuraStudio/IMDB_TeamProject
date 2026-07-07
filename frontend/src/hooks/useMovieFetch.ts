import {useState} from "react";
import {baseURI, type OmdbMovieResponse} from "../types/Movie.ts";
import axios from "axios";

export function useMovieFetch() {
    // This hook helps with conditional rendering
    const [isSubmitButtonClicked, setIsSubmitButtonClicked] = useState<boolean>(false);

    const [movie, setMovie] = useState<OmdbMovieResponse>({Title: "", Year: ""});
    const [isMovieValid, setIsMovieValid] = useState<boolean>(true);

    function submit(queryURI: string, pathSegment:string) {
        setIsSubmitButtonClicked(true);
        const validatedURI:string = baseURI + queryURI + encodeURIComponent(pathSegment);
        axios.get(validatedURI)
            .then((response) => {
                setMovie(response.data);
                setIsMovieValid(true);
            })
            .catch(() => setIsMovieValid(false));
    }

    return {isSubmitButtonClicked, isMovieValid, movie, submit};
}