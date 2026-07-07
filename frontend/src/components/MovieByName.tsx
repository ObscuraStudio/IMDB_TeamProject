import {type JSX, useEffect, useState} from "react";
import axios from "axios";
import {baseURI, MIN_LENGTH_MOVIE_TITLE, type OmdbMovieResponse} from "../types/Movie.ts";
import MovieDetails from "./MovieDetails.tsx";

export default function MovieByName() {
    // This hook helps with conditional rendering
    const [isSubmitButtonClicked, setIsSubmitButtonClicked] = useState<boolean>(false);
    // Hook is reset when navigating from another page
    useEffect(() => {
        setIsSubmitButtonClicked(false);
    }, []);

    const placeholder: string = "Enter movie name";
    const [movieName, setMovieName] = useState<string>(placeholder);
    const [movie, setMovie] = useState<OmdbMovieResponse>({Title: "", Year: ""});
    const [isMovieValid, setIsMovieValid] = useState<boolean>(true);

    function submitMovieName(event: React.SubmitEvent) {
        event.preventDefault();
        setIsSubmitButtonClicked(true);
        const queryURI: string = baseURI + "/title/" + movieName;
        axios.get(queryURI)
            .then((response) => {
                setMovie(response.data);
                setIsMovieValid(true);
            })
            .catch(() => setIsMovieValid(false));
    }


    const renderResultContent = ():JSX.Element => {
        return (
            <div>
                {isMovieValid ?
                    <MovieDetails {...movie}/> :
                    <p>Movie with given name not found</p>
                }
            </div>
        )
    }

    return (
        <>
            <form onSubmit={submitMovieName}>
                <label> Movie name </label>
                <br/>
                <input onChange=
                           {(e) =>
                               setMovieName(e.target.value)
                           }
                       type="text"
                       id="Movie name"
                       name="Movie name"
                       required
                       minLength={MIN_LENGTH_MOVIE_TITLE}
                       placeholder={placeholder}/>
                <br/>
                <button type={"submit"}> Submit</button>
            </form>
            {isSubmitButtonClicked && renderResultContent()}
        </>
    )
}