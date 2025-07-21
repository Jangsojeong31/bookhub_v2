import { Route } from 'react-router-dom'
import MainPage from '../views/main/Main'


function MainPageRoutes() {
  return (
    <>
    <Route path = "/main" element = {<MainPage />} />
    </>
  )
}

export default MainPageRoutes