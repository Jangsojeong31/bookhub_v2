import { Route } from 'react-router-dom'
import MainPage from './Main'


function Main() {
  return (
    <>
    <Route path = "/main" element = {<MainPage />} />
    </>
  )
}

export default Main