# documentation: http://doc.scrapy.org/en/latest/topics/items.html
from scrapy.item import Item, Field

class Page(Item):
    url = Field()
    title = Field()
    size = Field()
    referer = Field()
    newcookies = Field()
    body = Field()
