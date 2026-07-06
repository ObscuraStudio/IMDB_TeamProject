import type {OmdbMovieResponse} from "../types/Movie.ts";
import "../css/MovieTable.css"

export default function MovieDetails(movie: Readonly<OmdbMovieResponse>) {
    return (
        <>
            <div>
                <article>
                    <header>
                        <h1>
                            {movie.Title}
                        </h1>
                    </header>
                    <main>
                        <section id={"movie details"}>
                            <table className="interface-table">
                                <tbody>
                                    <tr key= "Director" className="interface-row">
                                        <td className="interface-label">Director</td>
                                        <td className="interface-value">{movie.Director}</td>
                                    </tr>
                                    <tr key= "Year" className="interface-row">
                                        <td className="interface-label">Year</td>
                                        <td className="interface-value">{movie.Year}</td>
                                    </tr>
                                    <tr key= "Genre" className="interface-row">
                                        <td className="interface-label">Genre</td>
                                        <td className="interface-value">{movie.Genre}</td>
                                    </tr>
                                    <tr key= "Writer" className="interface-row">
                                        <td className="interface-label">Writer</td>
                                        <td className="interface-value">{movie.Writer}</td>
                                    </tr>
                                    <tr key= "Actors" className="interface-row">
                                        <td className="interface-label">Actors</td>
                                        <td className="interface-value">{movie.Actors}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </section>
                    </main>
                </article>
            </div>
        </>
    )
}