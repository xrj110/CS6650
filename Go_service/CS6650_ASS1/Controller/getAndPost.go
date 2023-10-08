package Controller

import (
	"github.com/gin-gonic/gin"
	"net/http"
	"strconv"
)

var id = 1
var get = 1

type Profile struct {
	Artist string `json:"artist"`
	Title  string `json:"title"`
	Year   string `json:"year"`
}
type Response struct {
	ID   string `json:"albumID"`
	Size string `json:"imageSize"`
}

func Post(c *gin.Context) {
	var jsonData Profile
	jsonData.Year = c.PostForm("year")
	jsonData.Artist = c.PostForm("artist")
	jsonData.Title = c.PostForm("title")
	//_, err := c.FormFile("image")
	//if err != nil {
	//	c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
	//	return
	//}

	var res Response
	res.Size = "3475"
	res.ID = strconv.Itoa(id)
	id++
	c.JSON(http.StatusOK, res)

}
func Get(c *gin.Context) {
	albumID := c.Param("albumID")
	get++
	if albumID == "" {
		c.JSON(http.StatusNotFound, "the id not found")
	}
	_, err := strconv.Atoi(albumID)
	if err != nil {
		c.JSON(http.StatusBadRequest, "invalid")
	}
	//fmt.Println(get)
	var res Profile
	res.Year = "1997"
	res.Artist = "Sex Pistols"
	res.Title = "Never Mind The Bollocks!"
	c.JSON(http.StatusOK, res)
}
