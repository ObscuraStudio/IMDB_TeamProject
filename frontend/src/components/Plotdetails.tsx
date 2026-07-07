import type {OmdbMovieResponse} from "../types/Movie.ts";

export default function Plotdetails(movie: Readonly<OmdbMovieResponse>) {
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
                                <tr key="Plot" className="interface-row">
                                    <th className="row-header">Plot</th>
                                    <td className="interface-value">{movie.Plot}</td>
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