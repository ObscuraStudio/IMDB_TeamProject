export interface OmdbMovieResponse {
    Title?: string;
    Year?: string;
    Rated?: string;
    Released?: string;
    Runtime?: string;
    Genre?: string;
    Director?: string;
    Writer?: string;
    Actors?: string;
    Plot?: string;
    Language?: string;
    Country?: string;
    Awards?: string;
    Poster?: string;
    Ratings?: Rating[];
    Metascore?: string;
    imdbRating?: string;
    imdbVotes?: string;
    imdbID?: string;
    Type?: string;
    BoxOffice?: string;
    Response?: string;
    Error?: string;
}

export interface Rating {
    Source?: string;
    Value?: string;
}

export const baseURI: string = "/api/login/movies";
export const MIN_LENGTH_MOVIE_ID: number = 7;
export const SAFE_PATH_SEGMENT: RegExp = /^[a-zA-Z0-9\s'’:,.\-()]+$/;