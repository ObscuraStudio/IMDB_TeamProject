import type {OmdbMovieResponse} from "../types/Movie.ts";
import MovieDetails from "./MovieDetails.tsx";

type NameIdQueryResultProps = {
    isMovieValid: boolean,
    movie: OmdbMovieResponse
}

export default function NameIdQueryResult(props: Readonly<NameIdQueryResultProps>) {

    return (
        <div>
            {props.isMovieValid ?
                <MovieDetails {...props.movie}/> :
                <p>Movie with given name not found</p>
            }
        </div>
    )
}